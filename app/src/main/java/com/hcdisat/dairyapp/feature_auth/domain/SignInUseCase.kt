package com.hcdisat.dairyapp.feature_auth.domain

import com.hcdisat.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.abstraction.networking.ConfigureServices
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

interface SignInUseCase {
    suspend operator fun invoke(googleToken: String): Result<AccountSessionState>
}

class SignInUseCaseImpl @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val configureServices: ConfigureServices
) : SignInUseCase {
    override suspend fun invoke(googleToken: String): Result<AccountSessionState> = runCatching {
        when (val result = authRepository.firebaseGoogleSignIn(googleToken)) {
            is AccountSessionState.LoggedOut -> result

            is AccountSessionState.LoggedIn -> {
                configureServices.configureRealm()
                result
            }

            is AccountSessionState.Error -> {
                val error = result.reason
                if (error is CancellationException) throw error
                result
            }
        }
    }
}