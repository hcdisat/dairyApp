package com.hcdisat.dairyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.Session
import com.hcdisat.dairyapp.abstraction.networking.SessionService
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.domain.usecases.RetryImageUploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionService: SessionService,
    private val retryUploads: RetryImageUploadUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    fun getSession(): Session = sessionService.getSession()

    fun retryImageUpload() {
        viewModelScope.launch(dispatcher) { retryUploads() }
    }
}