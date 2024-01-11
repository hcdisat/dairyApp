package com.hcdisat.abstraction.navigation

import androidx.navigation.NavGraphBuilder

interface RouteApi<out T : Any> {
    val route: String

    fun register(
        navGraphBuilder: NavGraphBuilder,
        onEvent: (T) -> Unit
    )
}