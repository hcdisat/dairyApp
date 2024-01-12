package com.hcdisat.write

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hcdisat.abstraction.navigation.RouteApi
import com.hcdisat.common.settings.NavigationConstants
import com.hcdisat.write.ui.WriteScreen
import javax.inject.Inject

interface WriteRoute : RouteApi<Unit>

class WriteRouteImpl @Inject constructor() : WriteRoute {
    override val route: String
        get() = NavigationConstants.WRITE_ROUTE

    override fun register(navGraphBuilder: NavGraphBuilder, onEvent: (Unit) -> Unit) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(navArgument(name = NavigationConstants.WRITE_ARGUMENT) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            WriteScreen(onBackPressed = { onEvent(Unit) })
        }
    }
}