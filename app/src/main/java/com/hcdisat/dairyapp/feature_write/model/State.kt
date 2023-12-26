package com.hcdisat.dairyapp.feature_write.model

import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

sealed interface EntryScreenState {
    data object Loading : EntryScreenState
    data object Ready : EntryScreenState
    data class Error(val message: String) : EntryScreenState
    data object Saved : EntryScreenState
    data object Deleted : EntryScreenState
}

data class DiaryEntryState(
    val diaryEntry: PresentationDiary = PresentationDiary(),
    val screenState: EntryScreenState = EntryScreenState.Loading,
    val images: List<GalleryImage> = listOf()
)