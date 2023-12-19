package com.hcdisat.dairyapp.core

class UserNotAuthenticatedException : RuntimeException("User is not authenticated")
data class RealmGenericException(val throwable: Throwable) : RuntimeException(throwable)