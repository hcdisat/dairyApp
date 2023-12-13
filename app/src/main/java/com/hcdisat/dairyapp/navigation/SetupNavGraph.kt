@file:OptIn(ExperimentalMaterial3Api::class)

package com.hcdisat.dairyapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hcdisat.dairyapp.feature_auth.ui.AuthenticationScreen
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.ui.HomeScreen
import com.hcdisat.dairyapp.feature_write.ui.WriteScreen
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
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        HomeScreen(
            drawerState = drawerState,
            topBarScrollBehavior = scrollBehavior
        ) {
            when (this) {
                is HomeEvent.AddNewEntry -> onAddNewEntry(null)
                is HomeEvent.EditEntry -> onAddNewEntry(entryId)
                is HomeEvent.MenuClicked -> Unit
                is HomeEvent.OpenDrawer -> scope.launch { drawerState.open() }
                is HomeEvent.Logout -> onLoggedOut()
            }
        }
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
        WriteScreen(onBackPressed = onBackPressed)
    }
}