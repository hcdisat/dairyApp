package com.hcdisat.common.settings

object NavigationConstants {
    const val AUTHENTICATION_ROUTE = "authentication_screen"
    const val HOME_ROUTE = "home_screen"

    const val WRITE_ARGUMENT = "dairyId"
    const val WRITE_ROUTE = "write_screen?$WRITE_ARGUMENT={$WRITE_ARGUMENT}"
}