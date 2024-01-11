package com.hcdisat.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.auth.AuthenticationState
import com.hcdisat.auth.domain.SignInUseCase
import com.hcdisat.common.di.IODispatcher
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
internal class AuthenticationViewModel @Inject constructor(
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
                AuthenticationState(sessionState = it)
            }.fold(
                onSuccess = { _userSessionState.value = it },
                onFailure = {
                    if (it is CancellationException) throw it
                    _userSessionState.value = userSessionState.value.copy(
                        sessionState = AccountSessionState.Error(it)
                    )
                }
            )
        }
    }
}