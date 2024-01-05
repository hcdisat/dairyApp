package com.hcdisat.auth.ui

import com.hcdisat.abstraction.networking.AccountSessionState

internal data class AuthenticationState(
    val sessionState: AccountSessionState = AccountSessionState.LoggedOut(null),
    val loadingState: Boolean = false,
)