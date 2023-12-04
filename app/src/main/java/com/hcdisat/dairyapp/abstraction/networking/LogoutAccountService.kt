package com.hcdisat.dairyapp.abstraction.networking

sealed interface LogoutResult {
    data object Success : LogoutResult
    data class Failed(val reason: Throwable) : LogoutResult
}

interface LogoutAccountService {
    suspend fun logout(): LogoutResult
}