package com.hcdisat.dairyapp.feature_write.model

import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

sealed interface WriteEntryEvents {
    data object OnBackPressed : WriteEntryEvents
    data class OnDelete(val presentationDiary: PresentationDiary) : WriteEntryEvents
    data class OnTitleChanged(val newValue: String) : WriteEntryEvents
    data class OnDescriptionChanged(val newValue: String) : WriteEntryEvents
}