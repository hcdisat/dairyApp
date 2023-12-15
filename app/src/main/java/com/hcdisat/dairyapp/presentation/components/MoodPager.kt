package com.hcdisat.dairyapp.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.model.Mood

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MoodPager(
    modifier: Modifier = Modifier,
    selectedMood: Mood,
    onEvent: (WriteEntryEvents) -> Unit = {}
) {
    val images = Mood.entries

    val pagerState = rememberPagerState()
    LaunchedEffect(key1 = selectedMood) {
        pagerState.scrollToPage(selectedMood.ordinal)
    }

    val indexState by remember { derivedStateOf { pagerState.currentPage } }
    LaunchedEffect(key1 = indexState) {
        onEvent(WriteEntryEvents.OnMoodChanged(images[indexState]))
    }

    HorizontalPager(
        state = pagerState,
        count = images.size
    ) { index ->
        AsyncImage(
            modifier = modifier,
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(images[index].icon)
                .crossfade(true)
                .build(),
            contentDescription = images[index].name
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MoodPagerPreview() {
    MoodPager(modifier = Modifier.size(120.dp), Mood.Happy)
}