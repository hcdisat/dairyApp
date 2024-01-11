package com.hcdisat.home.model

import android.net.Uri
import androidx.compose.runtime.Stable
import com.hcdisat.ui.model.GalleryState
import com.hcdisat.ui.model.PresentationDiary

internal typealias HomeEventAction = HomeEvent.() -> Unit

internal sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object Logout : HomeEvent
    data object OpenDrawer : HomeEvent
    data object AddNewEntry : HomeEvent
    data object RemoveAll : HomeEvent
    data class EditEntry(val entryId: String) : HomeEvent
    data class ShowGallery(val diary: PresentationDiary) : HomeEvent
    data class LoadGallery(val diary: PresentationDiary) : HomeEvent
    data class HideGallery(val diary: PresentationDiary) : HomeEvent
    data class DiaryFilterEvent(val action: DiaryFilterAction) : HomeEvent
}

internal enum class DiaryFilterAction { AttachFilter, RemoveFilter }

internal sealed interface DiaryScreenState {
    data object Loading : DiaryScreenState
    data class Error(val throwable: Throwable) : DiaryScreenState
    data class Loaded(val isFiltered: Boolean = false) : DiaryScreenState
}

@Stable
internal data class DiaryState(
    val diaries: List<PresentationDiary> = listOf(),
    val galleryState: Map<String, GalleryStateData> = mapOf(),
    val screenState: DiaryScreenState = DiaryScreenState.Loading,
)

@Stable
internal data class GalleryStateData(
    val images: List<Uri> = listOf(),
    val galleryState: GalleryState = GalleryState.Collapsed
)

internal val DiaryState.isFiltered get() = (screenState as? DiaryScreenState.Loaded)?.isFiltered == true