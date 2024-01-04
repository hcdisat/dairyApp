package com.hcdisat.dairyapp.dataaccess.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storageMetadata
import com.hcdisat.abstraction.domain.model.ImageUploadResult
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

data class UploadSession(
    val sessionUri: Uri,
    val imageData: Uri,
    val remotePath: String,
    val totalByteCount: Long = 0,
    val bytesTransferred: Long = 0
)

interface ImageUploaderService {
    suspend fun uploadImagesWithResult(
        newImages: List<Pair<String, Uri>>,
        handleSession: (UploadSession?.() -> Unit) = {}
    ): ImageUploadResult

    suspend fun uploadImage(uploadTask: UploadTask): ImageUploadResult

    suspend fun resumeImageUpload(session: UploadSession): ImageUploadResult

    fun uploadImages(
        newImages: List<Pair<String, Uri>>,
        handleSession: UploadSession.() -> Unit = {}
    )
}

class ImageUploaderServiceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageUploaderService {
    override suspend fun uploadImagesWithResult(
        newImages: List<Pair<String, Uri>>,
        handleSession: (UploadSession?.() -> Unit)
    ): ImageUploadResult {
        var operationState: ImageUploadResult = ImageUploadResult.Success
        newImages.asSequence().forEach { (child, image) ->
            when (val result = uploadImage(child, image, handleSession)) {
                ImageUploadResult.Canceled -> {
                    operationState = result
                    return@forEach
                }

                is ImageUploadResult.Error -> throw result.throwable
                ImageUploadResult.Success -> Unit
            }
        }

        return operationState
    }

    override fun uploadImages(
        newImages: List<Pair<String, Uri>>,
        handleSession: UploadSession.() -> Unit
    ) {
        newImages.forEach { (child, image) ->
            storage.reference.child(child).putFile(image).addOnProgressListener { snapshot ->
                snapshot.uploadSessionUri?.let {
                    UploadSession(
                        sessionUri = it,
                        imageData = image,
                        remotePath = child,
                        totalByteCount = snapshot.totalByteCount,
                        bytesTransferred = snapshot.bytesTransferred
                    ).handleSession()
                }
            }
        }
    }

    override suspend fun uploadImage(uploadTask: UploadTask): ImageUploadResult =
        suspendCancellableCoroutine { continuation ->
            uploadTask.addOnCompleteListener {
                val exception = it.exception
                when {
                    exception != null -> continuation.resume(ImageUploadResult.Error(exception))
                    it.isSuccessful -> continuation.resume(ImageUploadResult.Success)
                    it.isCanceled -> continuation.resume(ImageUploadResult.Canceled)
                }
            }

            continuation.invokeOnCancellation { uploadTask.cancel() }
        }

    override suspend fun resumeImageUpload(session: UploadSession): ImageUploadResult {
        val uploadTask = getResumeUploadTask(session)
        return uploadImage(uploadTask)
    }

    private suspend fun uploadImage(
        child: String,
        image: Uri,
        handleSession: (UploadSession?.() -> Unit)
    ): ImageUploadResult {
        val uploadTask = storage.reference.child(child).putFile(image)
        uploadTask.addOnProgressListener {
            val session = it.uploadSessionUri?.let { uri ->
                UploadSession(
                    sessionUri = uri,
                    imageData = image,
                    remotePath = child,
                    totalByteCount = it.totalByteCount,
                    bytesTransferred = it.bytesTransferred
                )
            }

            handleSession(session)
        }

        return uploadImage(uploadTask)
    }

    private fun getResumeUploadTask(session: UploadSession): UploadTask =
        storage.reference.child(session.remotePath).putFile(
            session.imageData,
            storageMetadata { },
            session.sessionUri
        )
}