package com.hcdisat.dairyapp.dataaccess.firebase

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

interface DeleteImageService {
    suspend fun deleteImages(
        remotePaths: List<String>,
        onEach: (String) -> Unit = {},
        onEachDone: (String) -> Unit = {}
    )
}

class DeleteImageServiceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : DeleteImageService {
    override suspend fun deleteImages(
        remotePaths: List<String>,
        onEach: (String) -> Unit,
        onEachDone: (String) -> Unit
    ) {
        remotePaths.forEach { remoteImage ->
            onEach(remoteImage)
            suspendCancellableCoroutine { continuation ->
                storage.reference.child(remoteImage).delete().addOnSuccessListener {
                    onEachDone(remoteImage)
                    continuation.resumeWith(Result.success(Unit))
                }
            }
        }
    }
}