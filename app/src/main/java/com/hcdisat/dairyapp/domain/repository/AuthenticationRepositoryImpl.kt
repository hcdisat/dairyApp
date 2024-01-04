package com.hcdisat.dairyapp.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hcdisat.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.abstraction.domain.repository.FirebaseAuthResult
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.abstraction.networking.CreateAccountService
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseSignInService
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val createAccountService: CreateAccountService,
    private val firebaseSignInService: FirebaseSignInService,
) : AuthenticationRepository {
    override val user: FirebaseUser? get() = FirebaseAuth.getInstance().currentUser

    override suspend fun createWithGoogle(googleToken: String): AccountSessionState =
        createAccountService.createWithGoogle(googleToken)

    override suspend fun firebaseGoogleSignIn(googleToken: String): FirebaseAuthResult =
        firebaseSignInService.signInWithCredentials(googleToken)
}