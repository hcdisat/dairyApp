package com.hcdisat.dairyapp.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.model.Mood

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodPager(
    modifier: Modifier = Modifier,
    selectedMood: Mood,
    onEvent: (WriteEntryEvents) -> Unit = {}
) {
    val images = Mood.entries
    val pagerState = rememberPagerState { images.size }
    LaunchedEffect(key1 = selectedMood) {
        pagerState.scrollToPage(selectedMood.ordinal)
    }

    val indexState by remember { derivedStateOf { pagerState.currentPage } }
    LaunchedEffect(key1 = indexState) {
        onEvent(WriteEntryEvents.OnMoodChanged(images[indexState]))
    }

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = modifier
    ) { index ->
        Box(contentAlignment = Alignment.Center, modifier = modifier) {
            Image(
                painter = painterResource(id = images[index].icon),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun MoodPagerPreview() {
    MoodPager(selectedMood = Mood.Happy, modifier = Modifier.fillMaxWidth()) {}
}