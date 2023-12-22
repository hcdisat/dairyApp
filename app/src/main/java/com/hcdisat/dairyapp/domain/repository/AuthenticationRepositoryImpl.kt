package com.hcdisat.dairyapp.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hcdisat.dairyapp.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.abstraction.networking.CreateAccountService
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseAuthResult
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