package com.hcdisat.common.settings

object NavigationConstants {
    const val AUTHENTICATION_ROUTE = "auth"
    const val HOME_ROUTE = "home"

    const val WRITE_ARGUMENT = "dairyId"
    const val EDIT_WRITE_ROUTE = "write?${WRITE_ARGUMENT}="
    const val WRITE_ROUTE = "${EDIT_WRITE_ROUTE}={$WRITE_ARGUMENT}"
}