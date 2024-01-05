package com.hcdisat.write.model

import androidx.compose.runtime.Immutable
import com.hcdisat.ui.model.GalleryImage
import com.hcdisat.ui.model.PresentationDiary

sealed interface EntryScreenState {
    data object Loading : EntryScreenState
    data object Ready : EntryScreenState
    data class Error(val message: String) : EntryScreenState
    data object Saved : EntryScreenState
    data object Deleted : EntryScreenState
}

@Immutable
data class DiaryEntryState(
    val diaryEntry: PresentationDiary = PresentationDiary(),
    val screenState: EntryScreenState = EntryScreenState.Loading,
    val images: Set<GalleryImage> = setOf(),
    val newImages: List<GalleryImage> = listOf(),
    val imagesToRemove: Set<GalleryImage> = setOf()
)