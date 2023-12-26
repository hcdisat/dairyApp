package com.hcdisat.dairyapp.dataaccess.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadResult
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface ImageUploaderService {
    suspend fun uploadImages(newImages: List<Pair<String, Uri>>): ImageUploadResult
}

class ImageUploaderServiceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageUploaderService {
    override suspend fun uploadImages(newImages: List<Pair<String, Uri>>): ImageUploadResult {
        var operationState: ImageUploadResult = ImageUploadResult.Success
        newImages.asSequence().forEach { (child, image) ->
            when (val result = uploadImage(child, image)) {
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

    private suspend fun uploadImage(child: String, image: Uri): ImageUploadResult =
        suspendCancellableCoroutine { continuation ->
            val uploadTask = storage.reference.child(child).putFile(image)
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
}