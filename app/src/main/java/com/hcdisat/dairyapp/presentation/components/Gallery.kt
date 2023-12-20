package com.hcdisat.dairyapp.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.ui.theme.Elevation

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<String> = listOf(),
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    shape: CornerBasedShape = Shapes().small
) {
    BoxWithConstraints(modifier = modifier) {
        val imageCount by remember {
            derivedStateOf {
                maxOf(
                    a = 0,
                    b = this.maxWidth.div(imageSize + spaceBetween).toInt().minus(1)
                )
            }
        }

        val remainingImages by remember {
            derivedStateOf { images.size - imageCount }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceBetween)
        ) {
            images.take(imageCount).forEach { image ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.image_gallery_cd),
                    modifier = Modifier
                        .clip(shape)
                        .size(imageSize),
                    contentScale = ContentScale.Crop,
                )
            }

            if (remainingImages > 0) {
                ImagePagination(size = imageSize, shape = shape, remainingItems = remainingImages)
            }
        }
    }
}

@Composable
internal fun ImagePagination(size: Dp, shape: CornerBasedShape, remainingItems: Int) {
    Box(contentAlignment = Alignment.Center) {
        Surface(
            content = {},
            modifier = Modifier
                .clip(shape)
                .size(size)
                .background(MaterialTheme.colorScheme.primaryContainer),
            tonalElevation = Elevation.level4
        )
        Text(
            text = "+$remainingItems",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Preview
@Composable
private fun ImagePaginationPreview() {
    ImagePagination(
        size = 40.dp,
        shape = MaterialTheme.shapes.small,
        remainingItems = 4
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun GalleryPreview() {
    Gallery(images = listOf("", "", "", "", "", "", "", ""))
}