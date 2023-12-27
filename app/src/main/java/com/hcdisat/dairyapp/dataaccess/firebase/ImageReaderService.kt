package com.hcdisat.dairyapp.dataaccess.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.hcdisat.dairyapp.core.ImageNotFoundException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface ImageReaderService {
    suspend fun getImagesFromPaths(paths: List<String>): Result<List<Uri>>
}

class ImageReaderServiceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageReaderService {
    override suspend fun getImagesFromPaths(paths: List<String>): Result<List<Uri>> {
        return runCatching {
            paths.ifEmpty { listOf() }.map { path -> getDownloadUrl(path).getOrThrow() }
        }
    }

    private suspend fun getDownloadUrl(path: String): Result<Uri> =
        suspendCancellableCoroutine { continuation ->
            storage.reference.child(path.trim()).downloadUrl
                .addOnSuccessListener { maybeUri ->
                    val uri = maybeUri ?: throw ImageNotFoundException(path)
                    continuation.resume(Result.success(uri))
                }.addOnFailureListener { continuation.resumeWithException(it) }
        }
}
