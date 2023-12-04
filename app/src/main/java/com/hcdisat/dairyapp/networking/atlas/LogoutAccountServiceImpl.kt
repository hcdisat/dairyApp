package com.hcdisat.dairyapp.networking.atlas

import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.abstraction.networking.LogoutResult
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