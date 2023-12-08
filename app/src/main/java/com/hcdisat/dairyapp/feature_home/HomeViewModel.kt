package com.hcdisat.dairyapp.feature_home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_home.domain.usecase.GetDiariesUseCase
import com.hcdisat.dairyapp.feature_home.model.DiaryResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val logoutAccountService: LogoutAccountService,
    private val getDiaries: GetDiariesUseCase
) : ViewModel() {
    val homeState: MutableState<DiaryResult> = mutableStateOf(DiaryResult.Loading)

    init {
        observeDiaries()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(dispatcher) { logoutAccountService.logout() }
        }
    }

    private fun observeDiaries() {
        viewModelScope.launch {
            getDiaries().collect {
                homeState.value = it
            }
        }
    }
}