@file:OptIn(ExperimentalFoundationApi::class)

package com.hcdisat.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hcdisat.core.ui.R
import com.hcdisat.home.model.DiaryScreenState
import com.hcdisat.home.model.DiaryState
import com.hcdisat.home.model.GalleryStateData
import com.hcdisat.home.model.HomeEvent
import com.hcdisat.home.model.HomeEventAction
import com.hcdisat.ui.components.DiaryDate
import com.hcdisat.ui.components.DiaryHolder
import com.hcdisat.ui.components.LoadingContent
import com.hcdisat.ui.components.events.DiaryHolderEvent
import com.hcdisat.ui.extensions.toPresentationDate
import com.hcdisat.ui.model.PresentationDiary
import com.hcdisat.ui.model.entryKey


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    homeState: DiaryState = DiaryState(),
    paddingValues: PaddingValues = PaddingValues(all = 0.dp),
    onEvent: HomeEventAction,
) {
    when (homeState.screenState) {
        is DiaryScreenState.Loading -> LoadingContent()
        is DiaryScreenState.Error -> EmptyPage(
            title = "An error has occurred",
            subtitle = "We can't find any diaries, please login again."
        )

        is DiaryScreenState.Loaded -> LoadedContent(
            diaries = homeState.diaries,
            galleryState = homeState.galleryState,
            onEvent = onEvent,
            paddingValues = paddingValues
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun LoadedContent(
    diaries: List<PresentationDiary> = listOf(),
    galleryState: Map<String, GalleryStateData> = mapOf(),
    paddingValues: PaddingValues = PaddingValues(all = 0.dp),
    onEvent: HomeEventAction
) {
    if (diaries.isEmpty()) {
        EmptyPage(
            title = stringResource(R.string.empty_diary_title),
            subtitle = stringResource(R.string.write_something_title)
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        diaries.groupBy { it.dateTime.toPresentationDate() }.forEach { (date, entries) ->
            stickyHeader(key = date.entryKey) {
                DiaryDate(
                    date = date,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = 14.dp)
                )
            }

            items(items = entries, key = { it.id }) { diary ->
                val stateData = galleryState[diary.id] ?: GalleryStateData()
                DiaryHolder(
                    diary = diary,
                    onEvent = {
                        when (this) {
                            is DiaryHolderEvent.HideGallery ->
                                HomeEvent.HideGallery(diary).onEvent()

                            is DiaryHolderEvent.OnClicked ->
                                HomeEvent.EditEntry(diary.id).onEvent()

                            is DiaryHolderEvent.ShowGallery ->
                                HomeEvent.ShowGallery(diary).onEvent()
                        }
                    },
                    images = stateData.images,
                    galleryState = stateData.galleryState
                )
            }
        }
    }
}

@Composable
private fun EmptyPage(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        )
    }
}

class HomeContentProvider : PreviewParameterProvider<DiaryScreenState> {
    override val values: Sequence<DiaryScreenState>
        get() = sequenceOf(
            DiaryScreenState.Loading,
            DiaryScreenState.Error(Exception("Some Error")),
            DiaryScreenState.Loaded(),
        )
}

@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview(@PreviewParameter(HomeContentProvider::class) diaryResult: DiaryState) {
    HomeContent(diaryResult) {}
}