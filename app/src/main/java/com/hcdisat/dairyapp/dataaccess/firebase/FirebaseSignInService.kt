package com.hcdisat.dairyapp.dataaccess.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hcdisat.abstraction.domain.repository.FirebaseAuthResult
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface FirebaseSignInService {
    val user: FirebaseUser?
    suspend fun signInWithCredentials(tokenId: String): FirebaseAuthResult
}

class FirebaseSignInServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleCredentialService: GoogleCredentialsProvider
) : FirebaseSignInService {
    override val user: FirebaseUser? get() = FirebaseAuth.getInstance().currentUser

    override suspend fun signInWithCredentials(tokenId: String): FirebaseAuthResult =
        suspendCancellableCoroutine { continuation ->
            val credentials = googleCredentialService.getCredentials(tokenId)
            firebaseAuth.signInWithCredential(credentials).addOnCompleteListener { task ->
                when {
                    task.isSuccessful ->
                        continuation.resume(FirebaseAuthResult.Success)

                    task.isCanceled ->
                        continuation.resume(FirebaseAuthResult.Canceled)

                    else -> {
                        val error = task.exception ?: RuntimeException("Unknown Error")
                        continuation.resume(FirebaseAuthResult.Error(error))
                    }
                }

                continuation.invokeOnCancellation {
                    continuation.resume(FirebaseAuthResult.Canceled)
                }
            }
        }
}
