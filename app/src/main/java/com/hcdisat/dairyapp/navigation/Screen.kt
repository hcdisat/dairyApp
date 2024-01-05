package com.hcdisat.dairyapp.navigation

import com.hcdisat.common.settings.NavigationConstants

sealed class Screen(open val route: String) {
    data object Authentication : Screen(NavigationConstants.AUTHENTICATION_ROUTE)
    data object Home : Screen(NavigationConstants.HOME_ROUTE)
    data object Write : Screen(NavigationConstants.WRITE_ROUTE) {
        fun passDiaryId(entryId: String?): String = entryId?.let {
            "write_screen?${NavigationConstants.WRITE_ARGUMENT}=$it"
        } ?: NavigationConstants.WRITE_ROUTE
    }
}