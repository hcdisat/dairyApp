package com.hcdisat.dataaccess.realm.services

import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.abstraction.networking.CreateAccountService
import com.hcdisat.common.UserNotAuthenticatedException
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CreateAccountServiceImpl @Inject constructor(
    private val realmApp: App
) : CreateAccountService {
    override suspend fun createWithGoogle(googleToken: String): AccountSessionState =
        runCatching {
            if (realmApp.login(Credentials.jwt(googleToken)).loggedIn)
                AccountSessionState.LoggedIn
            else
                AccountSessionState.LoggedOut(UserNotAuthenticatedException())
        }.getOrElse {
            if (it is CancellationException) throw it
            it.printStackTrace()
            AccountSessionState.LoggedOut(it)
        }
}