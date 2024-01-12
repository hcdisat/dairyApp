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

fun interface Navigator {
    @Composable
    fun SetupNavGraph(
        startDestination: String,
        navHostController: NavHostController
    )
}

class NavigatorImpl @Inject constructor(
    private val authRoute: AuthenticationRoute,
    private val homeRoute: HomeRoute,
    private val writeRoute: WriteRoute
) : Navigator {
    @Composable
    override fun SetupNavGraph(
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