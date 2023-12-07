@file:OptIn(ExperimentalFoundationApi::class)

package com.hcdisat.dairyapp.feature_home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.presentation.components.DiaryDate
import com.hcdisat.dairyapp.presentation.components.DiaryHolder
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary


@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun HomeContent(
    diaries: Map<DairyPresentationDate, List<PresentationDiary>> = mapOf(),
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
            .fillMaxSize()
            .padding(24.dp)
    ) {
        diaries.forEach { (date, diaries) ->
            stickyHeader(key = date) { DiaryDate(date = date) }

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