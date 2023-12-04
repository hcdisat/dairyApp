package com.hcdisat.dairyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hcdisat.dairyapp.feature_auth.ui.AuthenticationScreen
import com.hcdisat.dairyapp.feature_auth.ui.AuthenticationViewModel
import com.hcdisat.dairyapp.feature_home.HomeScreen
import com.hcdisat.dairyapp.feature_home.HomeViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: Screen, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        home()
        write()
        authentication {
            navHostController.navigate(Screen.Home.route)
        }
    }
}

fun NavGraphBuilder.authentication(onLoginSuccess: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = hiltViewModel()
        val state = viewModel.userSessionState.collectAsState().value
        val messageBarState = rememberMessageBarState()
        val oneTapState = rememberOneTapSignInState()

        AuthenticationScreen(
            authenticationState = state,
            messageBarState = messageBarState,
            oneTapState = oneTapState,
            onButtonClick = { viewModel.setLoading(true) },
            onTokenIdReceived = { token -> viewModel.signInWithAtlas(token) },
            onDialogDismissed = {
                messageBarState.addError(Exception(it))
                viewModel.setLoading(false)
            },
            onLoginSuccess = onLoginSuccess
        )
    }
}

fun NavGraphBuilder.home() {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen {
            viewModel.logout()
        }
    }
}

fun NavGraphBuilder.write() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = NavigationConstants.WRITE_ARGUMENT) {
            type = NavType.StringType
            nullable = true
            defaultValue = true
        })
    ) {}
}