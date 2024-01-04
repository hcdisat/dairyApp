package com.hcdisat.dairyapp.feature_home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.core.ui.R
import com.hcdisat.dairyapp.feature_home.model.DiaryFilterAction
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isFiltered: Boolean = false,
    onEvent: HomeEventAction = {}
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = { Text(text = stringResource(R.string.home_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = { HomeEvent.MenuClicked.onEvent() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            val (action, icon) = if (isFiltered)
                DiaryFilterAction.RemoveFilter to Icons.Default.Clear
            else
                DiaryFilterAction.AttachFilter to Icons.Default.DateRange

            IconButton(onClick = { HomeEvent.DiaryFilterEvent(action).onEvent() }) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.sort_by_date_icon_cd),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}