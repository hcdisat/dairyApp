package com.hcdisat.dairyapp.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_home.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val logoutAccountService: LogoutAccountService,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            withContext(dispatcher) { logoutAccountService.logout() }
        }
    }
}