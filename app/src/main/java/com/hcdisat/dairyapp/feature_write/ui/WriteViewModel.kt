package com.hcdisat.dairyapp.feature_write.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.model.DiaryEntryState
import com.hcdisat.dairyapp.feature_write.model.EntryActions
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.navigation.NavigationConstants
import com.hcdisat.dairyapp.presentation.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val getSingleDiary: GetSingleDiaryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryEntryState())
    val state = _state.asStateFlow()

    init {
        handleLoadEntryEvent()
    }

    fun receiveAction(action: EntryActions) {
        when (action) {
            is EntryActions.UpdateDescription, is EntryActions.UpdateTitle -> updateText(action)
        }
    }

    private fun handleLoadEntryEvent() {
        val entryId = savedStateHandle.get<String>(NavigationConstants.WRITE_ARGUMENT)
        entryId?.let(::loadEntry) ?: DiaryEntryState.newState()
    }

    private fun loadEntry(entryId: String) {
        viewModelScope.launch(dispatcher) {
            _state.value = getSingleDiary(entryId).fold(
                onSuccess = { it },
                onFailure = {
                    Log.d("WriteViewModel", "loadEntry: ${it.message}", it)
                    state.value.copy(screenState = EntryScreenState.ERROR)
                }
            )
        }
    }

    private fun updateText(action: EntryActions): Unit = state.value.diaryEntry.update {
        if (action is EntryActions.UpdateTitle) {
            title = action.newValue
        }
        if (action is EntryActions.UpdateDescription) {
            description = action.newValue
        }
    }.let { _state.value = state.value.copy(diaryEntry = it) }
}