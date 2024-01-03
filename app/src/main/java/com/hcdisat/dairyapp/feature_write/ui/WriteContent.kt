package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.GalleryUploader
import com.hcdisat.dairyapp.presentation.components.GalleryUploaderEvents
import com.hcdisat.dairyapp.presentation.components.ImageModal
import com.hcdisat.dairyapp.presentation.components.ImageModalEvent
import com.hcdisat.dairyapp.presentation.components.MoodPager
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun WriteContent(
    modifier: Modifier = Modifier,
    diary: PresentationDiary = PresentationDiary(),
    images: Set<GalleryImage> = setOf(),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onEvent: WriteEntryEvents.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val scrollState = rememberScrollState()
        val placeHolderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .38f)
        val focusManager = LocalFocusManager.current
        val scope = rememberCoroutineScope()

        val imageModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var selectedImage by remember { mutableStateOf(GalleryImage()) }
        var shouldShowImageModal by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = shouldShowImageModal) {
            if (shouldShowImageModal) imageModalState.expand()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imePadding()
                .verticalScroll(state = scrollState)
        ) {
            MoodPager(
                modifier = Modifier.fillMaxWidth(),
                diary.mood,
                onEvent = onEvent
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = diary.title,
                placeholder = { Text(text = stringResource(R.string.entry_title_placeholder)) },
                onValueChange = { onEvent(WriteEntryEvents.OnTitleChanged(it)) },
                colors = textFieldColors(placeHolderColor = placeHolderColor),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                maxLines = 1,
                singleLine = true
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .onFocusEvent {
                        if (it.isFocused) {
                            scope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                    },
                value = diary.description,
                placeholder = { Text(text = stringResource(R.string.entry_description_placeholder)) },
                onValueChange = { onEvent(WriteEntryEvents.OnDescriptionChanged(it)) },
                colors = textFieldColors(placeHolderColor = placeHolderColor),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (diary.title.isNotBlank() && diary.description.isNotBlank())
                            WriteEntryEvents.OnSave(diary).onEvent()

                        focusManager.clearFocus()
                    }
                ),
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GalleryUploader(
                modifier = Modifier.padding(start = 12.dp),
                images = images,
                onEvents = {
                    when (this) {
                        is GalleryUploaderEvents.OnAddImageClicked -> Unit
                        is GalleryUploaderEvents.OnLocalImagesSelected ->
                            WriteEntryEvents.OnImagesAdded(this.images).onEvent()

                        is GalleryUploaderEvents.OnImageClicked -> {
                            selectedImage = image
                            shouldShowImageModal = true
                        }
                    }
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(54.dp),
                onClick = { WriteEntryEvents.OnSave(diary).onEvent() },
                shape = Shapes().small,
                enabled = diary.title.isNotBlank() && diary.description.isNotBlank()
            ) {
                Text(text = stringResource(R.string.save_btn))
            }
        }

        ImageModal(
            image = selectedImage,
            sheetState = imageModalState,
            showModal = shouldShowImageModal
        ) {
            when (it) {
                is ImageModalEvent.Dismiss -> {
                    shouldShowImageModal = false
                }

                is ImageModalEvent.OnDelete -> {
                    shouldShowImageModal = false
                    WriteEntryEvents.OnDeleteImage(it.image).onEvent()
                }
            }
        }
    }
}

@Composable
private fun textFieldColors(placeHolderColor: Color) = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Unspecified,

    focusedIndicatorColor = Color.Unspecified,
    unfocusedIndicatorColor = Color.Unspecified,
    disabledIndicatorColor = Color.Unspecified,

    disabledPlaceholderColor = placeHolderColor,
    focusedPlaceholderColor = placeHolderColor,
    unfocusedPlaceholderColor = placeHolderColor,
)