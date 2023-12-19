package com.hcdisat.dairyapp.feature_write.model

import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

sealed interface WriteEntryEvents {
    data object OnBackPressed : WriteEntryEvents
    data class OnSave(val entry: PresentationDiary) : WriteEntryEvents
    data class OnDelete(val presentationDiary: PresentationDiary) : WriteEntryEvents
    data class OnTitleChanged(val newValue: String) : WriteEntryEvents
    data class OnMoodChanged(val newValue: Mood) : WriteEntryEvents
    data class OnDescriptionChanged(val newValue: String) : WriteEntryEvents
    data class OnDateChanged(val dateInUtcMillis: Long) : WriteEntryEvents
    data class OnTimeChanged(val hour: Int, val minute: Int) : WriteEntryEvents
}