package com.hcdisat.dairyapp.feature_auth.ui

import com.hcdisat.abstraction.networking.AccountSessionState

data class AuthenticationState(
    val sessionState: AccountSessionState = AccountSessionState.LoggedOut(null),
    val loadingState: Boolean = false,
)