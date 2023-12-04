package com.hcdisat.dairyapp.feature_auth.ui

import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState

data class AuthenticationState(
    val sessionState: AccountSessionState = AccountSessionState.LOGGED_OUT,
    val loadingState: Boolean = false,
)