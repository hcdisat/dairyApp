package com.hcdisat.abstraction.domain.repository

import com.hcdisat.abstraction.networking.AccountSessionState

sealed interface FirebaseAuthResult {
    data object Success : FirebaseAuthResult
    data object Canceled : FirebaseAuthResult
    data class Error(val throwable: Throwable) : FirebaseAuthResult
}

interface AuthenticationRepository {
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
    suspend fun firebaseGoogleSignIn(googleToken: String): AccountSessionState
    fun getUId(): String?
}