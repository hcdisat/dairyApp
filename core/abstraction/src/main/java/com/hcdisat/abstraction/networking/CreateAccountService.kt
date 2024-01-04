package com.hcdisat.abstraction.networking

enum class AccountSessionState { LOGGED_IN, LOGGED_OUT, ERROR }

interface CreateAccountService {
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
}