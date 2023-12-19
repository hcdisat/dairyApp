package com.hcdisat.dairyapp.feature_write.model

import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

sealed interface EntryActions {
    data class UpdateTitle(val newValue: String) : EntryActions
    data class UpdateDescription(val newValue: String) : EntryActions
    data class UpdateMood(val newValue: Mood) : EntryActions
    data class SaveEntry(val entry: PresentationDiary) : EntryActions
    data class UpdateDate(val dateInUtcMillis: Long, val diary: PresentationDiary) : EntryActions
    data class DeleteEntry(val entry: PresentationDiary) : EntryActions
    data class UpdateTime(
        val hour: Int,
        val minute: Int,
        val diary: PresentationDiary
    ) : EntryActions

}