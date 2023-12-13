package com.hcdisat.dairyapp.feature_write.model

import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

enum class EntryScreenState { LOADING, READY, ERROR }
data class DiaryEntryState(
    val diaryEntry: PresentationDiary = PresentationDiary(),
    val screenState: EntryScreenState = EntryScreenState.LOADING
) {
    companion object {
        fun newState() = DiaryEntryState(
            screenState = EntryScreenState.READY,
            diaryEntry = PresentationDiary()
        )
    }
}
