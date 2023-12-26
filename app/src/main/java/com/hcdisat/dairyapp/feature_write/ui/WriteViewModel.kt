package com.hcdisat.dairyapp.feature_write.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadResult
import com.hcdisat.dairyapp.core.OperationCanceledException
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ErrorHandlerUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageData
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageExtension
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageUploaderUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.RemoteImagePathGeneratorUseCase
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
import kotlinx.coroutines.flow.StateFlow
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
    private val imagePathGenerator: RemoteImagePathGeneratorUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryEntryState())
    val state = _state.asStateFlow()

    init {
        handleLoadEntryEvent()
    }

    fun receiveAction(action: EntryActions) {
        when (action) {
            is EntryActions.AddImages -> handleImages(action.images)
            is EntryActions.DeleteEntry -> deleteEntry(action.entry.id)
            is EntryActions.UpdateTime -> updateTime(action.hour, action.minute, action.diary)
            is EntryActions.UpdateDate -> updateDate(action.dateInUtcMillis, action.diary)
            is EntryActions.SaveEntry -> saveEntry(action.entry)
            is EntryActions.UpdateMood -> updateMood(action.newValue)
            is EntryActions.UpdateDescription, is EntryActions.UpdateTitle -> updateText(action)
        }
    }

    private fun handleImages(newImages: List<Pair<Uri, String>>) = state.updateState {
        val remoteImageMetadata = newImages.map { (uri, ext) ->
            ImageData(uri) to ImageExtension(ext)
        }
        copy(images = imagePathGenerator(remoteImageMetadata))
    }

    private fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            state.updateState { copy(screenState = EntryScreenState.Loading) }
            withContext(dispatcher) { deleteDiary(entryId) }.fold(
                onFailure = ::handleError,
                onSuccess = { state.updateState { copy(screenState = EntryScreenState.Deleted) } }
            )
        }
    }

    private fun updateTime(hour: Int, minute: Int, diary: PresentationDiary) = state.updateState {
        val diaryEntry = diary.update {
            dateTime = dateTime.withHour(hour).withMinute(minute)
        }

        copy(diaryEntry = diaryEntry)
    }

    private fun updateDate(utcMillis: Long, diary: PresentationDiary) = state.updateState {
        updateDateTime(utcMillis = utcMillis, diary = diary).fold(
            onFailure = ::handleError,
            onSuccess = { copy(diaryEntry = it) }
        )
    }

    private fun saveEntry(entry: PresentationDiary) {
        state.updateState { copy(screenState = EntryScreenState.Loading) }
        viewModelScope.launch {
            uploadImages().mapCatching {
                val newEntry = entry.copy(images = state.value.images.map { it.remoteImagePath })
                saveDiary(newEntry).getOrThrow()
            }.fold(
                onFailure = ::handleError,
                onSuccess = {
                    _state.updateState {
                        copy(
                            diaryEntry = it,
                            screenState = EntryScreenState.Saved
                        )
                    }
                }
            )
        }
    }

    private suspend fun uploadImages(): Result<Unit> = withContext(dispatcher) {
        imageUploader(state.value.images).mapCatching { uploadResult ->
            when (uploadResult) {
                ImageUploadResult.Canceled ->
                    throw OperationCanceledException("Images upload cancelled")

                is ImageUploadResult.Error -> throw uploadResult.throwable
                ImageUploadResult.Success -> Unit
            }
        }
    }


    private fun updateMood(newMood: Mood) = state.updateState {
        val update = diaryEntry.update { mood = newMood }
        copy(diaryEntry = update)
    }

    private fun handleLoadEntryEvent() {
        val entryId = savedStateHandle.get<String>(NavigationConstants.WRITE_ARGUMENT)
        viewModelScope.launch {
            state.updateState {
                entryId?.let { loadEntry(it) } ?: copy(screenState = EntryScreenState.Ready)
            }
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

    private fun updateText(action: EntryActions) = state.updateState {
        val update = diaryEntry.update {
            if (action is EntryActions.UpdateTitle) {
                title = action.newValue
            }

            if (action is EntryActions.UpdateDescription) {
                description = action.newValue
            }
        }
        copy(diaryEntry = update)
    }

    private fun handleError(throwable: Throwable): DiaryEntryState {
        Log.d("WriteViewModel", "loadEntry: ${throwable.message}", throwable)
        return state.value.copy(screenState = EntryScreenState.Error(errorHandler(throwable)))
    }

    private inline fun StateFlow<DiaryEntryState>.updateState(updater: DiaryEntryState.() -> DiaryEntryState) {
        _state.value = value.updater()
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