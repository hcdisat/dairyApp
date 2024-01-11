package com.hcdisat.common.settings

object NavigationConstants {
    const val AUTHENTICATION_ROUTE = "auth"
    const val HOME_ROUTE = "home"

    const val WRITE_ARGUMENT = "dairyId"
    const val WRITE_ROUTE = "write?$WRITE_ARGUMENT={$WRITE_ARGUMENT}"
}