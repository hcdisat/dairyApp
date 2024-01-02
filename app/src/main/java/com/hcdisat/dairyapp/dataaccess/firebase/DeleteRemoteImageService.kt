package com.hcdisat.dairyapp.dataaccess.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface DeleteRemoteImageService {
    suspend fun deleteRemoteImages(
        remotePaths: List<String>,
        onEach: (String) -> Unit = {},
        onEachDone: (String) -> Unit = {}
    ): Result<Unit>

    suspend fun deleteRemoteImage(remotePath: String): Result<ImageDeletionResult>

    suspend fun deleteAllImages(onFailure: Throwable.(String) -> Unit): Result<Unit>
}

sealed class ImageDeletionResult(val remotePath: String) {
    data class Success(private val successPath: String) : ImageDeletionResult(successPath)
    data class PathNotFound(private val notFoundPath: String) : ImageDeletionResult(notFoundPath)
}

class DeleteImageServiceImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val signInService: FirebaseSignInService,
) : DeleteRemoteImageService {
    private val imagesDir get() = signInService.user?.let { "images/${it.uid}" }

    override suspend fun deleteRemoteImages(
        remotePaths: List<String>,
        onEach: (String) -> Unit,
        onEachDone: (String) -> Unit
    ) = runCatching {
        remotePaths.forEach { remoteImage ->
            onEach(remoteImage)
            deleteRemoteImage(remoteImage).onSuccess { onEachDone(remoteImage) }
        }
    }

    override suspend fun deleteRemoteImage(remotePath: String): Result<ImageDeletionResult> =
        suspendCancellableCoroutine { continuation ->
            runCatching {
                storage.reference.child(remotePath).delete().addOnCompleteListener {
                    continuation.resume(handleTask(it, remotePath))
                }
            }
        }

    override suspend fun deleteAllImages(
        onFailure: Throwable.(String) -> Unit
    ): Result<Unit> = coroutineScope {
        val directory = imagesDir ?: return@coroutineScope Result.success(Unit)
        val bucket = storage.reference
        val listAllTask = bucket.child(directory).listAll()

        runCatching {
            suspendCancellableCoroutine { continuation ->
                listAllTask.addOnSuccessListener { resultList ->
                    resultList.items
                        .asSequence()
                        .mapNotNull { ref -> ref?.let { "$directory/${it.name}" } }
                        .map { imagePath -> imagePath to bucket.child(imagePath).delete() }
                        .forEach { (path, task) ->
                            task.addOnFailureListener { launch { it.onFailure(path) } }
                        }

                    continuation.resume(Unit)
                }
            }
        }
    }

    private fun handleTask(task: Task<Void?>, targetPath: String): Result<ImageDeletionResult> {
        val error = task.exception
        return when {
            task.isSuccessful -> Result.success(ImageDeletionResult.Success(targetPath))

            error is StorageException && error.httpResultCode == 404 ->
                Result.success(ImageDeletionResult.PathNotFound(targetPath))

            error == null -> Result.success(ImageDeletionResult.Success(targetPath))

            else -> Result.failure(error)
        }
    }
}