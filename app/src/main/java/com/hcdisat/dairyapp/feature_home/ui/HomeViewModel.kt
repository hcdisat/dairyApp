package com.hcdisat.dairyapp.feature_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_home.domain.usecase.GetDiariesUseCase
import com.hcdisat.dairyapp.feature_home.model.DiaryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val logoutAccountService: LogoutAccountService,
    private val getDiaries: GetDiariesUseCase
) : ViewModel() {
    private val _homeState: MutableStateFlow<DiaryState> = MutableStateFlow(DiaryState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        observeDiaries()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(dispatcher) { logoutAccountService.logout() }
        }
    }

    private fun observeDiaries() {
        viewModelScope.launch(dispatcher) {
            getDiaries().collect { result ->
                _homeState.value = result.fold(
                    onSuccess = { DiaryState.Loaded(it) },
                    onFailure = { DiaryState.Error(it) }
                )
            }
        }
    }
}