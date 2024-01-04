package com.hcdisat.dairyapp.dataaccess.realm.services

import com.hcdisat.abstraction.networking.Session
import com.hcdisat.abstraction.networking.SessionService
import com.hcdisat.abstraction.networking.SessionState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import javax.inject.Inject

class SessionServiceImpl @Inject constructor(
    private val realmApp: App
) : SessionService {
    override fun getSession(): Session = realmApp.currentUser.toSession()

    private fun User?.toSession(): Session {
        return this?.let {
            Session(
                userId = id,
                state = if (loggedIn) SessionState.LOGGED_IN else SessionState.LOGGED_OUT
            )
        } ?: Session(null, SessionState.LOGGED_OUT)
    }
}