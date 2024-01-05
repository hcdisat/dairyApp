package com.hcdisat.abstraction.networking

sealed interface AccountSessionState {
    data object LoggedIn : AccountSessionState
    data class LoggedOut(val reason: Throwable?) : AccountSessionState
    data class Error(val reason: Throwable?) : AccountSessionState
}

interface CreateAccountService {
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
}