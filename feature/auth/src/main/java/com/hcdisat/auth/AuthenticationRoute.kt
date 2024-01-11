package com.hcdisat.auth

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hcdisat.abstraction.navigation.RouteApi
import com.hcdisat.auth.ui.AuthenticationScreen
import com.hcdisat.auth.ui.AuthenticationViewModel
import com.hcdisat.common.settings.NavigationConstants
import javax.inject.Inject

interface AuthenticationRoute : RouteApi<Unit>

class AuthenticationRouteImpl @Inject constructor() : AuthenticationRoute {
    override val route: String get() = NavigationConstants.AUTHENTICATION_ROUTE

    override fun register(
        navGraphBuilder: NavGraphBuilder,
        onEvent: (Unit) -> Unit
    ) {
        navGraphBuilder.composable(route = route) {
            val viewModel: AuthenticationViewModel = hiltViewModel()
            val state = viewModel.userSessionState.collectAsState().value
            AuthenticationScreen(
                state = state
            ) {
                when (it) {
                    is AuthUIEvents.LoadingStatus -> viewModel.setLoading(it.isLoading)
                    is AuthUIEvents.OnTokenReceived -> viewModel.signInWithAtlas(it.token)
                    is AuthUIEvents.SuccessLogin -> onEvent(Unit)
                }
            }
        }
    }
}