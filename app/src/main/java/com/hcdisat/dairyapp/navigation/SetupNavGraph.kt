@file:OptIn(ExperimentalMaterial3Api::class)

package com.hcdisat.dairyapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_auth.ui.AuthenticationScreen
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.ui.HomeScreen
import com.hcdisat.dairyapp.feature_home.ui.HomeViewModel
import com.hcdisat.dairyapp.feature_write.ui.WriteScreen
import com.hcdisat.dairyapp.presentation.components.AppAlertDialog
import com.hcdisat.dairyapp.presentation.components.DialogEvent
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(startDestination: Screen, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        write { navHostController.popBackStack() }
        authentication { navHostController.navigate(Screen.Home.route) }
        home(
            onAddNewEntry = {
                navHostController.navigate(Screen.Write.passDiaryId(it))
            },
            onLoggedOut = { navHostController.navigate(Screen.Authentication.route) }
        )
    }
}

fun NavGraphBuilder.authentication(onLoginSuccess: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        AuthenticationScreen(onLoginSuccess = onLoginSuccess)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.home(
    onAddNewEntry: (String?) -> Unit,
    onLoggedOut: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle(
            lifecycle = LocalLifecycleOwner.current.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )

        var isDialogDismissed by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        HomeScreen(
            drawerState = drawerState,
            diaryResult = homeState,
            topBarScrollBehavior = scrollBehavior
        ) {
            when (this) {
                is HomeEvent.AddNewEntry -> onAddNewEntry(null)
                is HomeEvent.EditEntry -> onAddNewEntry(entryId)
                is HomeEvent.MenuClicked -> Unit
                is HomeEvent.OpenDrawer -> scope.launch { drawerState.open() }
                is HomeEvent.Logout -> {
                    isDialogDismissed = false
                }
            }
        }

        AppAlertDialog(
            isDismissed = isDialogDismissed,
            message = stringResource(R.string.logout_dialog_message),
            title = stringResource(R.string.login_out_dialog_title),
            confirmButtonText = stringResource(R.string.yes_btn),
            dismissButtonText = stringResource(R.string.no_btn),
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

fun NavGraphBuilder.write(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = NavigationConstants.WRITE_ARGUMENT) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        WriteScreen(
            onBackPressed = onBackPressed,
            diary = null,
            onDelete = {}
        )
    }
}