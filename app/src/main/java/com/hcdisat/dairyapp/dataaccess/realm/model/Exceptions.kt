package com.hcdisat.dairyapp.dataaccess.realm.model

class UserNotAuthenticatedException : RuntimeException("User is not authenticated")
data class RealmGenericException(val throwable: Throwable) : RuntimeException(throwable)