package com.hcdisat.dairyapp.feature_write.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.core.InvalidImageUriException
import com.hcdisat.dairyapp.feature_write.model.EntryActions
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.DatePickerEvents
import com.hcdisat.dairyapp.presentation.components.DiaryDatePicker
import com.hcdisat.dairyapp.presentation.components.DiaryTimePicker
import com.hcdisat.dairyapp.presentation.components.LoadingContent
import com.hcdisat.dairyapp.presentation.components.TimePickerEvents
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.formattedDateTime
import com.hcdisat.dairyapp.presentation.extensions.toMillis

@Composable
fun WriteScreen(onBackPressed: () -> Unit) {
    val viewModel: WriteViewModel = hiltViewModel()
    val state by viewModel.state
        .collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)
    val context = LocalContext.current

    when (val screenState = state.screenState) {
        EntryScreenState.Loading -> LoadingContent()
        EntryScreenState.Ready -> {
            WriteScreen(diary = state.diaryEntry, images = state.images) { event ->
                when (event) {
                    WriteEntryEvents.OnBackPressed -> onBackPressed()
                    is WriteEntryEvents.OnDelete ->
                        viewModel.receiveAction(EntryActions.DeleteEntry(event.presentationDiary))

                    is WriteEntryEvents.OnDescriptionChanged ->
                        viewModel.receiveAction(EntryActions.UpdateDescription(event.newValue))

                    is WriteEntryEvents.OnTitleChanged ->
                        viewModel.receiveAction(EntryActions.UpdateTitle(event.newValue))

                    is WriteEntryEvents.OnMoodChanged ->
                        viewModel.receiveAction(EntryActions.UpdateMood(event.newValue))

                    is WriteEntryEvents.OnSave ->
                        viewModel.receiveAction(EntryActions.SaveEntry(event.entry))

                    is WriteEntryEvents.OnDateChanged ->
                        viewModel.receiveAction(
                            EntryActions.UpdateDate(
                                dateInUtcMillis = event.dateInUtcMillis,
                                diary = state.diaryEntry
                            )
                        )

                    is WriteEntryEvents.OnTimeChanged ->
                        viewModel.receiveAction(
                            EntryActions.UpdateTime(
                                hour = event.hour,
                                minute = event.minute,
                                diary = state.diaryEntry
                            )
                        )

                    is WriteEntryEvents.OnImagesAdded -> {
                        val images = event.images.map { uri -> uri to context.getImageType(uri) }
                        viewModel.receiveAction(EntryActions.AddImages(images))
                    }

                    is WriteEntryEvents.OnDeleteImage ->
                        viewModel.receiveAction(EntryActions.DeleteImage(event.galleryImage))
                }
            }
        }

        is EntryScreenState.Error ->
            LocalContext.current.showMessage(screenState.message)


        EntryScreenState.Deleted -> {
            onBackPressed()
            LocalContext.current.showMessage(R.string.diary_deleted_message)
        }

        EntryScreenState.Saved -> {
            onBackPressed()
            LocalContext.current.showMessage(R.string.diary_added_message)
        }
    }
}

@Composable
private fun WriteScreen(
    diary: PresentationDiary,
    images: Set<GalleryImage> = setOf(),
    onEvent: (WriteEntryEvents) -> Unit,
) {
    var shouldOpenDatePicker by rememberSaveable { mutableStateOf(false) }
    var shouldOpenTimePicker by rememberSaveable { mutableStateOf(false) }

    AppScaffold(
        topBar = {
            WriteTopBar(
                title = diary.mood.name,
                subtitle = diary.formattedDateTime,
                diaryTitle = diary.title,
                onBackPressed = { onEvent(WriteEntryEvents.OnBackPressed) },
                isEdit = diary.id.isNotBlank(),
                onDeletePressed = { onEvent(WriteEntryEvents.OnDelete(diary)) },
                onAddDatePressed = { shouldOpenDatePicker = true }
            )
        }
    ) {
        WriteContent(
            diary = diary,
            paddingValues = this,
            images = images,
            modifier = Modifier
                .padding(top = this.calculateTopPadding(), bottom = this.calculateTopPadding())
                .imePadding()
        ) { onEvent(this) }

        DiaryDatePicker(
            showDatePicker = shouldOpenDatePicker,
            selectedTimeInMillis = diary.dateTime.toMillis()
        ) {
            when (this) {
                is DatePickerEvents.DateSelected -> {
                    shouldOpenTimePicker = true
                    shouldOpenDatePicker = false
                    onEvent(WriteEntryEvents.OnDateChanged(this.dateInUtcMillis))
                }

                DatePickerEvents.OnDismissed -> {
                    shouldOpenDatePicker = false
                }
            }
        }

        DiaryTimePicker(
            showTimePicker = shouldOpenTimePicker,
            atHour = diary.dateTime.hour,
            atMinute = diary.dateTime.minute
        ) {
            when (this) {
                TimePickerEvents.OnDismissed -> {
                    shouldOpenTimePicker = false
                }

                is TimePickerEvents.TimeSelected -> {
                    shouldOpenTimePicker = false
                    onEvent(WriteEntryEvents.OnTimeChanged(hour, minute))
                }
            }
        }
    }
}

private fun Context.showMessage(@StringRes messageId: Int) {
    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
}

private fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

private fun Context.getImageType(uri: Uri): String {
    val type = contentResolver.getType(uri) ?: throw InvalidImageUriException(uri)
    return type.split("/").last()
}