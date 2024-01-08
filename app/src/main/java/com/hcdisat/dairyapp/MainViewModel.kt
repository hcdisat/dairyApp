package com.hcdisat.dairyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.abstraction.networking.Session
import com.hcdisat.abstraction.networking.SessionService
import com.hcdisat.common.di.IODispatcher
import com.hcdisat.domain.usecases.ResumeImagesRemovalUseCase
import com.hcdisat.domain.usecases.RetryImageUploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionService: SessionService,
    private val retryUploads: RetryImageUploadUseCase,
    private val deletePendingImage: ResumeImagesRemovalUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    fun getSession(): Session = sessionService.getSession()

    fun syncRemoteImages() {
        viewModelScope.launch(dispatcher) {
            launch { retryUploads() }
            launch { deletePendingImage() }
        }
    }
}