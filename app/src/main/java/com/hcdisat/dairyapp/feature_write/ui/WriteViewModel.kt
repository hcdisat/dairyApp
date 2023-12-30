package com.hcdisat.dairyapp.feature_write.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.domain.usecases.LoadDiaryGalleryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteImageUseCase
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
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
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
    private val deleteImage: DeleteImageUseCase,
    private val loadGallery: LoadDiaryGalleryUseCase,
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
            is EntryActions.DeleteImage -> removeImage(action.target)
            is EntryActions.AddImages -> addImages(action.images)
            is EntryActions.DeleteEntry -> deleteEntry(action.entry.id)
            is EntryActions.UpdateTime -> updateTime(action.hour, action.minute, action.diary)
            is EntryActions.UpdateDate -> updateDate(action.dateInUtcMillis, action.diary)
            is EntryActions.SaveEntry -> saveEntry(action.entry)
            is EntryActions.UpdateMood -> updateMood(action.newValue)
            is EntryActions.UpdateDescription, is EntryActions.UpdateTitle -> updateText(action)
        }
    }

    private fun removeImage(image: GalleryImage) = state.updateState {
        images.remove(image)
        newImages.remove(image)
        imagesToRemove.add(image)
    }

    private fun addImages(imagesToAdd: List<Pair<Uri, String>>) = state.updateState {
        val remoteImageMetadata = imagesToAdd.map { (uri, ext) ->
            ImageData(uri) to ImageExtension(ext)
        }.let(imagePathGenerator::invoke)

        images.addAll(remoteImageMetadata)
        newImages.addAll(remoteImageMetadata)
    }

    private fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            state.updateState { screenState = EntryScreenState.Loading }
            withContext(dispatcher) { deleteDiary(entryId) }.fold(
                onFailure = ::handleError,
                onSuccess = { state.updateState { screenState = EntryScreenState.Deleted } }
            )
        }
    }

    private fun updateTime(hour: Int, minute: Int, diary: PresentationDiary) = state.updateState {
        diaryEntry.dateTime = diary.dateTime.withHour(hour).withMinute(minute)
    }

    private fun updateDate(utcMillis: Long, diary: PresentationDiary) = state.updateState {
        updateDateTime(utcMillis = utcMillis, diary = diary).fold(
            onFailure = ::handleError,
            onSuccess = { diaryEntry = diary.toMutablePresentationDiary() }
        )
    }

    private fun saveEntry(entry: PresentationDiary) {
        state.updateState { screenState = EntryScreenState.Loading }
        viewModelScope.launch {
            launch(dispatcher) { imageUploader(state.value.newImages) }
            launch(dispatcher) { deleteImage(state.value.imagesToRemove) }

            val newEntry = entry.update {
                val imagesRemotePaths = state.value.images.map { it.remoteImagePath }.toMutableSet()
                imagesRemotePaths.addAll(images)
                images.apply {
                    clear()
                    addAll(imagesRemotePaths)
                }
            }

            withContext(dispatcher) { saveDiary(newEntry) }.fold(
                onFailure = ::handleError,
                onSuccess = {
                    state.updateState {
                        diaryEntry = it.toMutablePresentationDiary()
                        screenState = EntryScreenState.Saved
                        newImages.clear()
                    }
                }
            )
        }
    }

    private fun updateMood(newMood: Mood) = state.updateState {
        diaryEntry.mood = newMood
    }

    private fun handleLoadEntryEvent() {
        val entryId = savedStateHandle.get<String>(NavigationConstants.WRITE_ARGUMENT)
        viewModelScope.launch {
            entryId?.let { _state.value = loadEntry(it) } ?: run {
                state.updateState { screenState = EntryScreenState.Ready }
            }
        }
    }

    private suspend fun loadEntry(entryId: String): DiaryEntryState {
        return withContext(dispatcher) {
            getSingleDiary(entryId).mapCatching {
                val uriList = loadGallery(it.diaryEntry.images).getOrThrow()

                val galleryImages = it.diaryEntry.images.mapIndexed { index, remotePath ->
                    GalleryImage(image = uriList[index], remoteImagePath = remotePath)
                }.toSet()

                it.copy(images = galleryImages)
            }.fold(
                onFailure = ::handleError,
                onSuccess = { it },
            )
        }
    }

    private fun updateText(action: EntryActions) = state.updateState {
        when (action) {
            is EntryActions.UpdateDescription -> diaryEntry.description = action.newValue
            is EntryActions.UpdateTitle -> diaryEntry.title = action.newValue
            else -> Unit
        }
    }

    private fun handleError(throwable: Throwable): DiaryEntryState {
        Log.d("WriteViewModel", "loadEntry: ${throwable.message}", throwable)
        return state.value.copy(screenState = EntryScreenState.Error(errorHandler(throwable)))
    }

    private inline fun StateFlow<DiaryEntryState>.updateState(updater: MutableState.() -> Unit) {
        val mutableState = value.toMutableState()
        mutableState.updater()
        _state.value = mutableState.toState()
    }

    private fun PresentationDiary.update(
        mutableDiaryScope: MutablePresentationDiary.() -> Unit
    ): PresentationDiary {
        val diary = toMutablePresentationDiary()
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

    private fun PresentationDiary.toMutablePresentationDiary(): MutablePresentationDiary =
        MutablePresentationDiary(
            id = id,
            title = title,
            description = description,
            mood = mood,
            images = images.toMutableList(),
            dateTime = dateTime
        )

    private fun MutableState.toState() = DiaryEntryState(
        diaryEntry = diaryEntry.toPresentationDiary(),
        screenState = screenState,
        images = images.toSet(),
        newImages = newImages.toList(),
        imagesToRemove = imagesToRemove.toSet()
    )

    private fun DiaryEntryState.toMutableState() = MutableState(
        diaryEntry = diaryEntry.toMutablePresentationDiary(),
        screenState = screenState,
        images = images.toMutableSet(),
        newImages = newImages.toMutableList(),
        imagesToRemove = imagesToRemove.toMutableSet()
    )
}

private data class MutableState(
    var diaryEntry: MutablePresentationDiary = MutablePresentationDiary(),
    var screenState: EntryScreenState = EntryScreenState.Loading,
    val images: MutableSet<GalleryImage> = mutableSetOf(),
    val newImages: MutableList<GalleryImage> = mutableListOf(),
    val imagesToRemove: MutableSet<GalleryImage> = mutableSetOf()
)