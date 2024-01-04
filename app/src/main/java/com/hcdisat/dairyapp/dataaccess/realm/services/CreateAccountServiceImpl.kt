package com.hcdisat.dairyapp.dataaccess.realm.services

import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.abstraction.networking.CreateAccountService
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
                AccountSessionState.LOGGED_IN
            else
                AccountSessionState.LOGGED_OUT
        }.getOrElse {
            if (it is CancellationException) throw it
            it.printStackTrace()
            AccountSessionState.ERROR
        }
}