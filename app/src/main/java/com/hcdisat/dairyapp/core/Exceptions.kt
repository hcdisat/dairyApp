package com.hcdisat.dairyapp.core

class UserNotAuthenticatedException : RuntimeException("User is not authenticated")
class SignInCancelledException : RuntimeException("Sign in operation was cancelled.")
class UnexpectedException(innerException: Throwable) : RuntimeException(innerException)
data class RealmGenericException(val throwable: Throwable) : RuntimeException(throwable)