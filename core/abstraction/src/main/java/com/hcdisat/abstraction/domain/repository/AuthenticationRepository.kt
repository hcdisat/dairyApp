package com.hcdisat.abstraction.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.hcdisat.abstraction.networking.AccountSessionState

sealed interface FirebaseAuthResult {
    data object Success : FirebaseAuthResult
    data object Canceled : FirebaseAuthResult
    data class Error(val throwable: Throwable) : FirebaseAuthResult
}

interface AuthenticationRepository {
    val user: FirebaseUser?
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
    suspend fun firebaseGoogleSignIn(googleToken: String): FirebaseAuthResult
}