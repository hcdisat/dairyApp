package com.hcdisat.dairyapp.feature_home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onEvent: HomeEventAction) {
    TopAppBar(
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
            IconButton(onClick = { HomeEvent.Logout.onEvent() }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.sort_by_date_icon_cd),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar {}
}