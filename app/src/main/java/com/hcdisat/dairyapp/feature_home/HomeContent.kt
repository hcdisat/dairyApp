@file:OptIn(ExperimentalFoundationApi::class)

package com.hcdisat.dairyapp.feature_home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.DiaryResult
import com.hcdisat.dairyapp.presentation.components.DiaryDate
import com.hcdisat.dairyapp.presentation.components.DiaryHolder
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary


@Composable
fun HomeContent(
    homeState: DiaryResult = DiaryResult.Loaded(mapOf()),
    paddingValues: PaddingValues = PaddingValues(all = 0.dp),
    onClick: (String) -> Unit = {}
) {
    when (homeState) {
        is DiaryResult.Loading -> LoadingContent()
        is DiaryResult.Error -> EmptyPage(
            title = "An error has occurred",
            subtitle = "We can't find any diaries, please login again."
        )

        is DiaryResult.Loaded -> LoadedContent(
            diaries = homeState.diaries,
            onClick = onClick,
            paddingValues = paddingValues
        )
    }


}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@ExperimentalFoundationApi
@Composable
private fun LoadedContent(
    diaries: Map<DairyPresentationDate, List<PresentationDiary>> = mapOf(),
    paddingValues: PaddingValues = PaddingValues(all = 0.dp),
    onClick: (String) -> Unit = {}
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
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        diaries.forEach { (date, diaries) ->
            stickyHeader(key = date.toString()) {
                DiaryDate(
                    date = date,
                    modifier = Modifier.padding(vertical = 14.dp)
                )
            }

            items(items = diaries, key = { it.id }) { diary ->
                DiaryHolder(diary = diary, onClick = { it?.let(onClick) })
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

class HomeContentProvider : PreviewParameterProvider<DiaryResult> {
    override val values: Sequence<DiaryResult>
        get() = sequenceOf(
            DiaryResult.Loading,
            DiaryResult.Error(Exception("Some Error")),
            DiaryResult.Loaded(mapOf()),
        )
}

@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview(@PreviewParameter(HomeContentProvider::class) diaryResult: DiaryResult) {
    HomeContent(diaryResult) {}
}