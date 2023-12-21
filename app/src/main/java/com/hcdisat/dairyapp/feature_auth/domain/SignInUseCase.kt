package com.hcdisat.dairyapp.feature_auth.domain

import com.hcdisat.dairyapp.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.core.SignInCancelledException
import com.hcdisat.dairyapp.core.UnexpectedException
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseAuthResult
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

interface SignInUseCase {
    suspend operator fun invoke(googleToken: String): Result<AccountSessionState>
}

class SignInUseCaseImpl @Inject constructor(
    private val authRepository: AuthenticationRepository
) : SignInUseCase {
    override suspend fun invoke(googleToken: String): Result<AccountSessionState> = runCatching {
        when (val result = authRepository.firebaseGoogleSignIn(googleToken)) {
            is FirebaseAuthResult.Canceled -> throw SignInCancelledException()
            is FirebaseAuthResult.Error -> {
                throw if (result.throwable is CancellationException) result.throwable
                else UnexpectedException(result.throwable)
            }

            is FirebaseAuthResult.Success -> authRepository.createWithGoogle(googleToken)
        }
    }
}