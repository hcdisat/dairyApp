package com.hcdisat.dairyapp.presentation.components.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class GalleryState {
    private val _images = mutableListOf<GalleryImage>()
    private val _imagesTobeDeleted = mutableListOf<GalleryImage>()

    val images get() = _images.toList()
    val imagesTobeDeleted get() = _imagesTobeDeleted.toList()

    fun addImage(galleryImage: GalleryImage) {
        _images.add(galleryImage)
    }

    fun addImages(galleryImages: List<GalleryImage>) {
        _images.addAll(galleryImages)
    }

    fun addImageUris(galleryImages: List<Uri>) {
        galleryImages.map {
            GalleryImage(image = it)
        }.also { addImages(it) }
    }

    fun removeImage(galleryImage: GalleryImage) {
        _imagesTobeDeleted.add(galleryImage)
        _images.remove(galleryImage)
    }

    override fun hashCode(): Int {
        var result = _images.hashCode()
        result = 31 * result + _imagesTobeDeleted.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GalleryState

        if (_images != other._images) return false
        return _imagesTobeDeleted == other._imagesTobeDeleted
    }
}

data class GalleryImage(val image: Uri, val remoteImagePath: String = "")

@Composable
fun rememberGalleryUploaderState(): GalleryState = remember { GalleryState() }