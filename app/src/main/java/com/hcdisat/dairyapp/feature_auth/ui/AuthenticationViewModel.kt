package com.hcdisat.dairyapp.feature_auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.CreateAccountService
import com.hcdisat.dairyapp.core.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val createAccountService: CreateAccountService
) : ViewModel() {
    private val _userSessionState = MutableStateFlow(AuthenticationState())
    val userSessionState = _userSessionState.asStateFlow()

    fun setLoading(isLoading: Boolean) = _userSessionState.update {
        it.copy(loadingState = isLoading)
    }

    fun signInWithAtlas(tokenId: String) {
        viewModelScope.launch {
            val sessionState = withContext(dispatcher) {
                createAccountService.createWithGoogle(tokenId)
            }
            _userSessionState.update { it.copy(sessionState = sessionState, loadingState = false) }
        }
    }
}