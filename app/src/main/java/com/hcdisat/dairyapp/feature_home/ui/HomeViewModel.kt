package com.hcdisat.dairyapp.feature_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.core.di.IODispatcher
import com.hcdisat.dairyapp.domain.usecases.LoadDiaryGalleryUseCase
import com.hcdisat.dairyapp.feature_home.domain.usecase.GetDiariesUseCase
import com.hcdisat.dairyapp.feature_home.model.DiaryScreenState
import com.hcdisat.dairyapp.feature_home.model.DiaryState
import com.hcdisat.dairyapp.feature_home.model.GalleryState
import com.hcdisat.dairyapp.feature_home.model.GalleryStateData
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val logoutAccountService: LogoutAccountService,
    private val getDiaries: GetDiariesUseCase,
    private val loadGallery: LoadDiaryGalleryUseCase
) : ViewModel() {
    private val _homeState: MutableStateFlow<DiaryState> = MutableStateFlow(DiaryState())
    val homeState = _homeState.asStateFlow()

    init {
        observeDiaries()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(dispatcher) { logoutAccountService.logout() }
        }
    }

    fun hideGalleryImages(presentationDiary: PresentationDiary) = updateGallery(presentationDiary) {
        copy(galleryState = GalleryState.Collapsed)
    }

    fun showGalleryImages(presentationDiary: PresentationDiary) {
        if (homeState.value.galleryState.contains(presentationDiary.id)) {
            updateGallery(presentationDiary) {
                copy(galleryState = GalleryState.Visible)
            }
            return
        }

        loadImageGallery(presentationDiary)
    }

    fun loadImageGallery(presentationDiary: PresentationDiary) {
        updateGallery(presentationDiary) {
            copy(galleryState = GalleryState.Loading)
        }

        viewModelScope.launch {
            withContext(dispatcher) { loadGallery(presentationDiary.images) }.fold(
                onFailure = {
                    updateGallery(presentationDiary) {
                        copy(galleryState = GalleryState.Error(it))
                    }
                },
                onSuccess = { imageUris ->
                    updateGallery(presentationDiary) {
                        copy(images = imageUris, galleryState = GalleryState.Visible)
                    }
                }
            )
        }
    }

    fun removeAllDiaries() {

    }

    private fun observeDiaries() {
        viewModelScope.launch {
            withContext(dispatcher) { getDiaries() }.collect { result ->
                result.fold(
                    onSuccess = { diaries ->
                        updateState {
                            diaries.filter { diary ->
                                diary.id in galleryState.filter {
                                    it.value.galleryState == GalleryState.Visible
                                }.keys
                            }.forEach(::loadImageGallery)

                            copy(diaries = diaries, screenState = DiaryScreenState.Loaded)
                        }
                    },
                    onFailure = {
                        updateState { copy(screenState = DiaryScreenState.Error(it)) }
                    }
                )
            }
        }
    }

    private inline fun updateState(update: DiaryState.() -> DiaryState) {
        _homeState.value = homeState.value.update()
    }

    private inline fun updateGallery(
        presentationDiary: PresentationDiary,
        update: GalleryStateData.() -> GalleryStateData
    ) = updateState {
        val galleryToUpdate = galleryState[presentationDiary.id] ?: GalleryStateData()

        val updatedGallery = galleryToUpdate.update()
        val newGalleryMap = galleryState.toMutableMap()
        newGalleryMap[presentationDiary.id] = updatedGallery

        copy(galleryState = newGalleryMap.toMap())
    }
}