package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcdisat.dairyapp.feature_write.model.EntryActions
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.LoadingContent
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.formattedDateTime

@Composable
fun WriteScreen(onBackPressed: () -> Unit) {
    val viewModel: WriteViewModel = hiltViewModel()
    val state by viewModel.state
        .collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)

    when (state.screenState) {
        EntryScreenState.LOADING -> LoadingContent()
        EntryScreenState.READY -> {
            WriteScreen(diary = state.diaryEntry) { event ->
                when (event) {
                    is WriteEntryEvents.OnBackPressed -> onBackPressed()
                    is WriteEntryEvents.OnDelete -> {}
                    is WriteEntryEvents.OnDescriptionChanged ->
                        viewModel.receiveAction(EntryActions.UpdateDescription(event.newValue))

                    is WriteEntryEvents.OnTitleChanged ->
                        viewModel.receiveAction(EntryActions.UpdateTitle(event.newValue))

                    is WriteEntryEvents.OnMoodChanged ->
                        viewModel.receiveAction(EntryActions.UpdateMood(event.newValue))
                }
            }
        }

        EntryScreenState.ERROR -> {
            throw Exception("UPPS")
        }
    }
}

@Composable
private fun WriteScreen(
    diary: PresentationDiary,
    onEvent: (WriteEntryEvents) -> Unit,
) {
    AppScaffold(
        topBar = {
            WriteTopBar(
                title = diary.mood.name,
                subtitle = diary.formattedDateTime,
                diaryTitle = diary.title,
                onBackPressed = { onEvent(WriteEntryEvents.OnBackPressed) },
                isEdit = diary.id.isNotBlank(),
                onDeletePressed = { onEvent(WriteEntryEvents.OnDelete(diary)) }
            )
        }
    ) {
        WriteContent(diary = diary, paddingValues = this) { onEvent(this) }
    }
}