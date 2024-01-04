package com.hcdisat.ui.model

import android.net.Uri

sealed interface GalleryState {
    data object Loading : GalleryState
    data object Visible : GalleryState
    data object Collapsed : GalleryState
    data class Error(val throwable: Throwable) : GalleryState
}

data class GalleryImage(val image: Uri = Uri.EMPTY, val remoteImagePath: String = "")