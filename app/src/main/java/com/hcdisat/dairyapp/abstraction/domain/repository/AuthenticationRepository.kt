package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseAuthResult

interface AuthenticationRepository {
    suspend fun createWithGoogle(googleToken: String): AccountSessionState
    suspend fun firebaseGoogleSignIn(googleToken: String): FirebaseAuthResult
}