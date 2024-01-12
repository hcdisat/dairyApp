package com.hcdisat.write.model

import android.net.Uri
import com.hcdisat.ui.model.GalleryImage
import com.hcdisat.ui.model.Mood
import com.hcdisat.ui.model.PresentationDiary

internal sealed interface WriteEntryEvents {
    data object OnBackPressed : WriteEntryEvents
    data class OnSave(val entry: PresentationDiary) : WriteEntryEvents
    data class OnDelete(val presentationDiary: PresentationDiary) : WriteEntryEvents
    data class OnTitleChanged(val newValue: String) : WriteEntryEvents
    data class OnMoodChanged(val newValue: Mood) : WriteEntryEvents
    data class OnDescriptionChanged(val newValue: String) : WriteEntryEvents
    data class OnDateChanged(val dateInUtcMillis: Long) : WriteEntryEvents
    data class OnTimeChanged(val hour: Int, val minute: Int) : WriteEntryEvents
    data class OnImagesAdded(val images: List<Uri>) : WriteEntryEvents
    data class OnDeleteImage(val galleryImage: GalleryImage) : WriteEntryEvents
}