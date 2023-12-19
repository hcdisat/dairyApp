package com.hcdisat.dairyapp.feature_write.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCase
import com.hcdisat.dairyapp.feature_write.model.DiaryEntryState
import com.hcdisat.dairyapp.feature_write.model.EntryActions
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.navigation.NavigationConstants
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val getSingleDiary: GetSingleDiaryUseCase,
    private val saveDiary: SaveDiaryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryEntryState())
    val state = _state.asStateFlow()

    init {
        handleLoadEntryEvent()
    }

    fun receiveAction(action: EntryActions) {
        when (action) {
            is EntryActions.SaveEntry -> saveEntry(action.entry)
            is EntryActions.UpdateMood -> updateMood(action.newValue)
            is EntryActions.UpdateDescription, is EntryActions.UpdateTitle -> updateText(action)
            is EntryActions.UpdateDate -> {}
        }
    }

    private fun saveEntry(entry: PresentationDiary) {
        viewModelScope.launch {
            _state.value = withContext(dispatcher) { saveDiary(entry) }.fold(
                onFailure = ::handleError,
                onSuccess = {
                    state.value.copy(diaryEntry = it, screenState = EntryScreenState.SAVED)
                },
            )
        }
    }

    private fun updateMood(newMood: Mood) {
        val update = _state.value.diaryEntry.update { mood = newMood }
        _state.value = state.value.copy(diaryEntry = update)
    }

    private fun handleLoadEntryEvent() {
        val entryId = savedStateHandle.get<String>(NavigationConstants.WRITE_ARGUMENT)
        entryId?.let {
            viewModelScope.launch { _state.value = loadEntry(it) }
        } ?: run {
            _state.value = DiaryEntryState.newState().copy(screenState = EntryScreenState.READY)
        }
    }

    private suspend fun loadEntry(entryId: String): DiaryEntryState {
        return withContext(dispatcher) {
            getSingleDiary(entryId).fold(
                onFailure = ::handleError,
                onSuccess = { it },
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

    private fun handleError(throwable: Throwable): DiaryEntryState {
        Log.d("WriteViewModel", "loadEntry: ${throwable.message}", throwable)
        return state.value.copy(screenState = EntryScreenState.ERROR)
    }
}