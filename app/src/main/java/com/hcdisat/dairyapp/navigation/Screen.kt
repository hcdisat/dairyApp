package com.hcdisat.dairyapp.navigation

sealed class Screen(open val route: String) {
    data object Authentication : Screen(NavigationConstants.AUTHENTICATION_ROUTE)
    data object Home : Screen(NavigationConstants.HOME_ROUTE)
    data class Write(val entryId: String? = null) : Screen(NavigationConstants.WRITE_ROUTE) {
        override val route: String
            get() = entryId?.let {
                NavigationConstants.WRITE_ROUTE.replace(
                    oldValue = "{${NavigationConstants.WRITE_ARGUMENT}}",
                    newValue = it
                )
            } ?: super.route

        companion object : Screen(NavigationConstants.WRITE_ROUTE)
    }
}