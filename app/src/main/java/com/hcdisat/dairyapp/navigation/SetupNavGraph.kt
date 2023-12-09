package com.hcdisat.dairyapp.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.hcdisat.dairyapp.presentation.components.AppAlertDialog
import com.hcdisat.dairyapp.presentation.components.DialogEvent
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(startDestination: Screen, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        write()
        authentication { navHostController.navigate(Screen.Home.route) }
        home(
            onAddNewEntry = { navHostController.navigate(Screen.Write(it).route) },
            onLoggedOut = { navHostController.navigate(Screen.Authentication.route) }
        )
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

fun NavGraphBuilder.home(
    onAddNewEntry: (String?) -> Unit,
    onLoggedOut: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var isDialogDismissed by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle(
            lifecycle = LocalLifecycleOwner.current.lifecycle,
            minActiveState = Lifecycle.State.RESUMED
        )

        HomeScreen(
            drawerState = drawerState,
            diaryResult = homeState,
        ) {
            when (this) {
                is HomeEvent.AddNewEntry -> onAddNewEntry(this.entryId)
                is HomeEvent.MenuClicked -> Unit
                is HomeEvent.OpenDrawer -> scope.launch { drawerState.open() }
                is HomeEvent.Logout -> {
                    isDialogDismissed = false
                }
            }
        }

        AppAlertDialog(
            isDismissed = isDialogDismissed,
            message = "You are about to logout from the Dairy... Do you want to proceeded?",
            title = "Login out",
            confirmButtonText = "Yes",
            dismissButtonText = "No",
            onEvent = {
                isDialogDismissed = true
                if (it == DialogEvent.POSITIVE) {
                    viewModel.logout()
                    onLoggedOut()
                }
            }
        )
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