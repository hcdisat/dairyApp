package com.hcdisat.write.model

import android.net.Uri
import com.hcdisat.ui.model.GalleryImage
import com.hcdisat.ui.model.Mood
import com.hcdisat.ui.model.PresentationDiary

internal sealed interface EntryActions {
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

    data class AddImages(val images: List<Pair<Uri, String>>) : EntryActions
    data class DeleteImage(val target: GalleryImage) : EntryActions
}