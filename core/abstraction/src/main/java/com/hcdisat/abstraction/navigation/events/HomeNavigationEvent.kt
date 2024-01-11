package com.hcdisat.abstraction.navigation.events

sealed interface HomeNavigationEvent {
    data object AddNewEntry : HomeNavigationEvent
    data object Logout : HomeNavigationEvent
    data class EditEntry(val entryId: String) : HomeNavigationEvent
}