package com.hcdisat.dairyapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ImageModal(
    image: GalleryImage = GalleryImage(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    showModal: Boolean = true,
    onEvent: (ImageModalEvent) -> Unit = {},
) {

    AnimatedVisibility(
        visible = showModal,
        enter = fadeIn() + expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(ImageModalEvent.Dismiss) },
            sheetState = sheetState
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEvent(ImageModalEvent.OnDelete(image)) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = Icons.Default.Delete.name
                    )
                }

                IconButton(onClick = { onEvent(ImageModalEvent.Dismiss) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = Icons.Default.Close.name
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image.image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.image_gallery_cd),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

sealed interface ImageModalEvent {
    data object Dismiss : ImageModalEvent
    data class OnDelete(val image: GalleryImage) : ImageModalEvent
}