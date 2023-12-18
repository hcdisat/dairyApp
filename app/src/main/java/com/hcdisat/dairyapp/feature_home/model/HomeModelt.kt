package com.hcdisat.dairyapp.feature_home.model

import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

typealias HomeEventAction = HomeEvent.() -> Unit

sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object Logout : HomeEvent
    data object OpenDrawer : HomeEvent
    data object AddNewEntry : HomeEvent
    data class EditEntry(val entryId: String) : HomeEvent
}

sealed class DiaryState(open val diaries: List<PresentationDiary>? = null) {
    data object Loading : DiaryState()
    data class Error(val throwable: Throwable) : DiaryState()
    data class Loaded(override val diaries: List<PresentationDiary>) :
        DiaryState(diaries)
}