package com.hcdisat.dairyapp.abstraction.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseAuthResult

interface AuthenticationRepository {
    val user: FirebaseUser?
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
    suspend fun firebaseGoogleSignIn(googleToken: String): FirebaseAuthResult
}