package com.hcdisat.dairyapp.navigation

sealed class Screen(val route: String) {
    data object Authentication: Screen(NavigationConstants.AUTHENTICATION_ROUTE)
    data object Home: Screen(NavigationConstants.HOME_ROUTE)
    data object Write: Screen(NavigationConstants.WRITE_ROUTE)
}