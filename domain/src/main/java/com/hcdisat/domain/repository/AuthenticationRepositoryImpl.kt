package com.hcdisat.domain.repository

import com.hcdisat.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.abstraction.domain.repository.FirebaseAuthResult
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.abstraction.networking.CreateAccountService
import com.hcdisat.common.OperationCanceledException
import com.hcdisat.dataaccess.firebase.FirebaseSignInService
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val createAccountService: CreateAccountService,
    private val firebaseSignInService: FirebaseSignInService
) : AuthenticationRepository {
    override suspend fun createWithGoogle(googleToken: String): AccountSessionState =
        createAccountService.createWithGoogle(googleToken)

    override suspend fun firebaseGoogleSignIn(googleToken: String): AccountSessionState =
        when (val result = firebaseSignInService.signInWithCredentials(googleToken)) {
            is FirebaseAuthResult.Canceled ->
                AccountSessionState.LoggedOut(OperationCanceledException())

            is FirebaseAuthResult.Error -> AccountSessionState.Error(result.throwable)
            FirebaseAuthResult.Success -> createWithGoogle(googleToken)
        }

    override fun getUId(): String? = firebaseSignInService.uid
}