package com.hcdisat.dairyapp.feature_home.model

typealias HomeEventAction = HomeEvent.() -> Unit

sealed interface HomeEvent {
    data object MenuClicked : HomeEvent
    data object None : HomeEvent
    data object OpenDrawer : HomeEvent
    data class AddNewEntry(val entryId: String? = null) : HomeEvent
}