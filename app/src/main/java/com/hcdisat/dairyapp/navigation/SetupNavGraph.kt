package com.hcdisat.dairyapp.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(startDestination: Screen, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        write()
        home {
            navHostController.navigate(Screen.Write(it).route)
        }
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

fun NavGraphBuilder.home(onAddNewEntry: (String?) -> Unit) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        HomeScreen(drawerState = drawerState) {
            when (this) {
                is HomeEvent.AddNewEntry -> onAddNewEntry(this.entryId)
                is HomeEvent.MenuClicked -> Unit
                is HomeEvent.None -> viewModel.logout()
                is HomeEvent.OpenDrawer -> scope.launch { drawerState.open() }
            }
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
    ) {
        Log.d(
            "NavGraphBuilder",
            "write: ${it.arguments?.getString(NavigationConstants.WRITE_ARGUMENT)}"
        )
    }
}