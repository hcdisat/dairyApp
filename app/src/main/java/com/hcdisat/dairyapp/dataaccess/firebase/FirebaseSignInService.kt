package com.hcdisat.dairyapp.dataaccess.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface FirebaseAuthResult {
    data object Success : FirebaseAuthResult
    data object Canceled : FirebaseAuthResult
    data class Error(val throwable: Throwable) : FirebaseAuthResult
}

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
        suspendCoroutine { continuation ->
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
            }
        }
}
