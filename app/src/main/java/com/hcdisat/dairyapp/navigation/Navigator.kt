package com.hcdisat.dairyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hcdisat.abstraction.navigation.RouteApi
import com.hcdisat.abstraction.navigation.events.HomeNavigationEvent
import com.hcdisat.auth.AuthenticationRoute
import com.hcdisat.common.settings.NavigationConstants
import com.hcdisat.home.HomeRoute
import com.hcdisat.write.WriteRoute
import javax.inject.Inject

class Navigator @Inject constructor(
    private val authRoute: AuthenticationRoute,
    private val homeRoute: HomeRoute,
    private val writeRoute: WriteRoute
) {
    @Composable
    fun SetupNavGraph(
        startDestination: String,
        navHostController: NavHostController
    ) {
        NavHost(navController = navHostController, startDestination = startDestination) {
            registerAuthentication(navHostController)
            registerHome(navHostController)
            registerWrite(navHostController)
        }
    }

    private fun <T : Any> NavGraphBuilder.register(
        routeApi: RouteApi<T>,
        onRouteEvent: (T) -> Unit
    ) {
        routeApi.register(navGraphBuilder = this, onEvent = onRouteEvent)
    }

    private fun NavGraphBuilder.registerAuthentication(
        navHostController: NavHostController
    ) {
        register(
            routeApi = authRoute,
            onRouteEvent = {
                navHostController.navigate(homeRoute.route)
            }
        )
    }

    private fun NavGraphBuilder.registerHome(navHostController: NavHostController) {
        register(homeRoute) {
            when (it) {
                is HomeNavigationEvent.AddNewEntry ->
                    navHostController.navigate(NavigationConstants.WRITE_ROUTE)

                is HomeNavigationEvent.EditEntry ->
                    navHostController.navigate(homeRoute.passDiaryId(it.entryId))

                is HomeNavigationEvent.Logout ->
                    navHostController.navigate(NavigationConstants.AUTHENTICATION_ROUTE)
            }
        }
    }

    private fun NavGraphBuilder.registerWrite(navHostController: NavHostController) {
        register(writeRoute) { navHostController.navigate(homeRoute.route) }
    }
}

//@Composable
//fun SetupNavGraph(startDestination: Screen, navHostController: NavHostController) {
//    NavHost(navController = navHostController, startDestination = startDestination.route) {
//        registerWrite { navHostController.popBackStack() }
//        //authentication { navHostController.navigate(Screen.Home.route) }
////        home(
////            onAddNewEntry = {
////                navHostController.navigate(Screen.Write.route)
////            },
////            onEditEntry = {
////                navHostController.navigate(Screen.Write.passDiaryId(it))
////            },
////            onLoggedOut = { navHostController.navigate(Screen.Authentication.route) }
////        )
//    }
//}

//fun NavGraphBuilder.registerWrite(onBackPressed: () -> Unit) {
//    composable(
//        route = Screen.Write.route,
//        arguments = listOf(navArgument(name = NavigationConstants.WRITE_ARGUMENT) {
//            type = NavType.StringType
//            nullable = true
//            defaultValue = null
//        })
//    ) {
//        WriteScreen(onBackPressed = onBackPressed)
//    }
//}