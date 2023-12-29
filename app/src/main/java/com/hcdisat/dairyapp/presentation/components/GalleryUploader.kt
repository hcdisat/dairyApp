package com.hcdisat.dairyapp.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import com.hcdisat.dairyapp.ui.theme.Elevation

@Preview
@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier,
    images: Set<GalleryImage> = setOf(),
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = MaterialTheme.shapes.medium,
    spaceBetween: Dp = 12.dp,
    onEvents: GalleryUploaderEvents.() -> Unit = {}
) {
    val context = LocalContext.current
    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION

    val multiplePhotoPicker = rememberMultiplePhotoPicker {
        forEach { context.contentResolver.takePersistableUriPermission(it, flag) }
        GalleryUploaderEvents.OnLocalImagesSelected(this).onEvents()
    }

    BoxWithConstraints(modifier = modifier) {
        val imageCount by remember {
            derivedStateOf {
                maxOf(
                    a = 0,
                    b = this.maxWidth.div(imageSize + spaceBetween).toInt().minus(2)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceBetween)
        ) {
            AddImageButton(size = imageSize, shape = imageShape) {
                GalleryUploaderEvents.OnAddImageClicked.onEvents()
                multiplePhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            images.take(imageCount).forEach { galleryImage ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(galleryImage.image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.image_gallery_cd),
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize)
                        .clickable {
                            GalleryUploaderEvents
                                .OnImageClicked(galleryImage)
                                .onEvents()
                        },
                    contentScale = ContentScale.Crop,
                )
            }

            val remainingImages = images.size - imageCount
            if (remainingImages > 0) {
                ImagePagination(
                    size = imageSize,
                    shape = imageShape,
                    remainingItems = remainingImages
                )
            }
        }
    }
}

@Composable
private fun AddImageButton(size: Dp, shape: CornerBasedShape, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .clip(shape)
                .size(size)
                .background(MaterialTheme.colorScheme.primaryContainer),
            tonalElevation = Elevation.level4,
            onClick = onClick
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add images")
        }
    }
}

@Preview
@Composable
private fun AddImageButtonPreview() {
    AddImageButton(
        size = 60.dp,
        shape = MaterialTheme.shapes.small,
    ) {}
}


sealed interface GalleryUploaderEvents {
    data object OnAddImageClicked : GalleryUploaderEvents
    data class OnLocalImagesSelected(val images: List<Uri>) : GalleryUploaderEvents
    data class OnImageClicked(val image: GalleryImage) : GalleryUploaderEvents
}

@Composable
fun rememberMultiplePhotoPicker(onResult: List<Uri>.() -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10),
    onResult = onResult
)