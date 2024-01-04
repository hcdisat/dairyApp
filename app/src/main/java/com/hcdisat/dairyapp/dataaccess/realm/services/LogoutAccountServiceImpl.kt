package com.hcdisat.dairyapp.dataaccess.realm.services

import com.hcdisat.abstraction.networking.LogoutAccountService
import com.hcdisat.abstraction.networking.LogoutResult
import io.realm.kotlin.mongodb.App
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LogoutAccountServiceImpl @Inject constructor(
    private val realmApp: App
) : LogoutAccountService {
    override suspend fun logout(): LogoutResult = runCatching {
        realmApp.currentUser?.logOut()
        LogoutResult.Success
    }.getOrElse {
        when (it) {
            is CancellationException -> throw it
            else -> LogoutResult.Failed(it)
        }
    }
}