package com.hcdisat.auth

import com.hcdisat.abstraction.networking.AccountSessionState

internal data class AuthenticationState(
    val sessionState: AccountSessionState = AccountSessionState.LoggedOut(null),
    val loadingState: Boolean = false,
)

sealed interface AuthUIEvents {
    data object SuccessLogin : AuthUIEvents
    data class LoadingStatus(val isLoading: Boolean) : AuthUIEvents
    data class OnTokenReceived(val token: String) : AuthUIEvents
}