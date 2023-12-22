package com.hcdisat.dairyapp.feature_write.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ErrorHandlerUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageUploaderUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.UpdateDateTimeUseCase
import com.hcdisat.dairyapp.feature_write.model.DiaryEntryState
import com.hcdisat.dairyapp.feature_write.model.EntryActions
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.navigation.NavigationConstants
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.MutablePresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
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
    private val saveDiary: SaveDiaryUseCase,
    private val updateDateTime: UpdateDateTimeUseCase,
    private val deleteDiary: DeleteDiaryUseCase,
    private val imageUploader: ImageUploaderUseCase,
    private val errorHandler: ErrorHandlerUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryEntryState())
    val state = _state.asStateFlow()

    init {
        handleLoadEntryEvent()
    }

    fun receiveAction(action: EntryActions) {
        when (action) {
            is EntryActions.AddImages -> handleImages(action.diaryEntry, action.images)
            is EntryActions.DeleteEntry -> deleteEntry(action.entry.id)
            is EntryActions.UpdateTime -> updateTime(action.hour, action.minute, action.diary)
            is EntryActions.UpdateDate -> updateDate(action.dateInUtcMillis, action.diary)
            is EntryActions.SaveEntry -> saveEntry(action.entry)
            is EntryActions.UpdateMood -> updateMood(action.newValue)
            is EntryActions.UpdateDescription, is EntryActions.UpdateTitle -> updateText(action)
        }
    }

    private fun handleImages(diaryEntry: PresentationDiary, newImages: List<Pair<Uri, String>>) {
        imageUploader(newImages).fold(
            onFailure = ::handleError,
            onSuccess = { galleryImages ->
                val updatedDiary = diaryEntry.update {
                    galleryImages.map { it.remoteImagePath }.also { images.addAll(it) }
                }
                _state.value = state.value.copy(images = galleryImages, diaryEntry = updatedDiary)
                Log.d("hector", "handleImages: ${state.value}")
            }
        )
    }

    private fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(screenState = EntryScreenState.Loading)
            withContext(dispatcher) { deleteDiary(entryId) }.fold(
                onFailure = ::handleError,
                onSuccess = {
                    _state.value = state.value.copy(screenState = EntryScreenState.Deleted)
                }
            )
        }
    }

    private fun updateTime(hour: Int, minute: Int, diary: PresentationDiary) {
        _state.value = state.value.copy(
            diaryEntry = diary.update {
                dateTime = dateTime.withHour(hour).withMinute(minute)
            }
        )
    }

    private fun updateDate(utcMillis: Long, diary: PresentationDiary) {
        updateDateTime(utcMillis = utcMillis, diary = diary).fold(
            onFailure = ::handleError,
            onSuccess = { _state.value = state.value.copy(diaryEntry = it) }
        )
    }

    private fun saveEntry(entry: PresentationDiary) {
        viewModelScope.launch {
            _state.value = withContext(dispatcher) {
                val newEntry = entry.copy(
                    images = state.value.images.map { it.remoteImagePath }
                )
                saveDiary(newEntry)
            }.fold(
                onFailure = ::handleError,
                onSuccess = {
                    state.value.copy(diaryEntry = it, screenState = EntryScreenState.Saved)
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
            _state.value = DiaryEntryState.newState().copy(screenState = EntryScreenState.Ready)
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
        return state.value.copy(screenState = EntryScreenState.Error(errorHandler(throwable)))
    }

    private fun PresentationDiary.update(
        mutableDiaryScope: MutablePresentationDiary.() -> Unit
    ): PresentationDiary {
        val diary = MutablePresentationDiary(
            id = id,
            title = title,
            description = description,
            mood = mood,
            images = images.toMutableList(),
            dateTime = dateTime
        )

        diary.mutableDiaryScope()
        return diary.toPresentationDiary()
    }

    private fun MutablePresentationDiary.toPresentationDiary(): PresentationDiary =
        PresentationDiary(
            id = id,
            title = title,
            description = description,
            mood = mood,
            images = images,
            dateTime = dateTime
        )
}