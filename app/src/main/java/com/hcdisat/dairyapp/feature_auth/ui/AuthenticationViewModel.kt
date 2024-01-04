package com.hcdisat.dairyapp.feature_auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.di.IODispatcher
import com.hcdisat.dairyapp.feature_auth.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val signIn: SignInUseCase
) : ViewModel() {
    private val _userSessionState = MutableStateFlow(AuthenticationState())
    val userSessionState = _userSessionState.asStateFlow()

    fun setLoading(isLoading: Boolean) = _userSessionState.update {
        it.copy(loadingState = isLoading)
    }

    fun signInWithAtlas(tokenId: String) {
        viewModelScope.launch {
            withContext(dispatcher) { signIn(tokenId) }.mapCatching {
                when (it) {
                    AccountSessionState.LOGGED_IN ->
                        AuthenticationState(sessionState = AccountSessionState.LOGGED_IN)

                    else ->
                        AuthenticationState(sessionState = AccountSessionState.LOGGED_OUT)
                }
            }.fold(
                onSuccess = { _userSessionState.value = it },
                onFailure = {
                    if (it is CancellationException) throw it
                    _userSessionState.value = userSessionState.value.copy(
                        sessionState = AccountSessionState.ERROR
                    )
                }
            )
        }
    }
}