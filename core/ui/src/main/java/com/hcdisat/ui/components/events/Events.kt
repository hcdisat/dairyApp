package com.hcdisat.ui.components.events

import com.hcdisat.ui.model.PresentationDiary

sealed interface DiaryHolderEvent {
    data class OnClicked(val entryId: String) : DiaryHolderEvent
    data class ShowGallery(val diary: PresentationDiary) : DiaryHolderEvent
    data class HideGallery(val diary: PresentationDiary) : DiaryHolderEvent
}