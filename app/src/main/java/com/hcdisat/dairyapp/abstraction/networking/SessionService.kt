package com.hcdisat.dairyapp.abstraction.networking

enum class SessionState { LOGGED_IN, LOGGED_OUT }
data class Session(val userId: String? = null, val state: SessionState)

interface SessionService {
    fun getSession(): Session
}