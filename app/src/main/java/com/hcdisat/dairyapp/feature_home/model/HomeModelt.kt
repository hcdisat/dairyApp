package com.hcdisat.dairyapp.feature_home.model

import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

typealias HomeEventAction = HomeEvent.() -> Unit

sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object Logout : HomeEvent
    data object OpenDrawer : HomeEvent
    data class AddNewEntry(val entryId: String? = null) : HomeEvent
}

sealed class DiaryResult(open val diaries: Map<DairyPresentationDate, List<PresentationDiary>>? = null) {
    data object Loading : DiaryResult()
    data class Error(val throwable: Throwable) : DiaryResult()
    data class Loaded(override val diaries: Map<DairyPresentationDate, List<PresentationDiary>>) :
        DiaryResult(diaries)
}