package com.hcdisat.dairyapp.feature_home.model

import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

typealias HomeEventAction = HomeEvent.() -> Unit
typealias Diaries = Map<DairyPresentationDate, List<PresentationDiary>>


sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object Logout : HomeEvent
    data object OpenDrawer : HomeEvent
    data class AddNewEntry(val entryId: String? = null) : HomeEvent
}

sealed class DiaryResult(open val diaries: Diaries? = null) {
    data object Loading : DiaryResult()
    data class Error(val throwable: Throwable) : DiaryResult()
    data class Loaded(override val diaries: Diaries) : DiaryResult(diaries)
}