package com.hcdisat.dairyapp.dataaccess.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
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
}

sealed class ImageDeletionResult(val remotePath: String) {
    data class Success(private val successPath: String) : ImageDeletionResult(successPath)
    data class PathNotFound(private val notFoundPath: String) : ImageDeletionResult(notFoundPath)
}

class DeleteImageServiceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : DeleteRemoteImageService {
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