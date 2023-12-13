package com.hcdisat.dairyapp.feature_write.model

sealed interface EntryActions {
    data class UpdateTitle(val newValue: String) : EntryActions
    data class UpdateDescription(val newValue: String) : EntryActions
}