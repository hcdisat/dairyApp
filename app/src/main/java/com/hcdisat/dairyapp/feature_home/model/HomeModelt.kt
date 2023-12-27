package com.hcdisat.dairyapp.feature_home.model

import android.net.Uri
import androidx.compose.runtime.Stable
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

typealias HomeEventAction = HomeEvent.() -> Unit

sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object Logout : HomeEvent
    data object OpenDrawer : HomeEvent
    data object AddNewEntry : HomeEvent
    data class EditEntry(val entryId: String) : HomeEvent
    data class ShowGallery(val diary: PresentationDiary) : HomeEvent
    data class LoadGallery(val diary: PresentationDiary) : HomeEvent
    data class HideGallery(val diary: PresentationDiary) : HomeEvent
}

sealed interface DiaryScreenState {
    data object Loading : DiaryScreenState
    data class Error(val throwable: Throwable) : DiaryScreenState
    data object Loaded : DiaryScreenState
}

sealed interface GalleryState {
    data object Loading : GalleryState
    data object Visible : GalleryState
    data object Collapsed : GalleryState
    data class Error(val throwable: Throwable) : GalleryState
}

@Stable
data class DiaryState(
    val diaries: List<PresentationDiary> = listOf(),
    val galleryState: Map<String, GalleryStateData> = mapOf(),
    val screenState: DiaryScreenState = DiaryScreenState.Loading,
)

@Stable
data class GalleryStateData(
    val images: List<Uri> = listOf(),
    val galleryState: GalleryState = GalleryState.Collapsed
)