package com.hcdisat.dairyapp

import androidx.lifecycle.ViewModel
import com.hcdisat.dairyapp.abstraction.networking.Session
import com.hcdisat.dairyapp.abstraction.networking.SessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionService: SessionService
) : ViewModel() {
    fun getSession(): Session = sessionService.getSession()
}