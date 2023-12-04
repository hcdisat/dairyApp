package com.hcdisat.dairyapp.feature_home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction
import com.hcdisat.dairyapp.presentation.components.AppNavigationDrawer
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.Logout
import com.hcdisat.dairyapp.presentation.components.NavigationDrawerEvent

@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onEvent: HomeEventAction,
) {
    AppNavigationDrawer(
        drawerState = drawerState,
        onEvent = {
            when (this) {
                NavigationDrawerEvent.Logout -> HomeEvent.None.onEvent()
            }
        }
    ) {
        AppScaffold(
            topBar = { HomeTopBar(onEvent = { HomeEvent.OpenDrawer.onEvent() }) },
            floatingAction = {
                FloatingActionButton(onClick = { HomeEvent.AddNewEntry().onEvent() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.add_new_note_cd)
                    )
                }
            }
        ) {
            Logout { HomeEvent.None.onEvent() }
        }
    }
}


data class HomeScreenProvider(
    override val values: Sequence<DrawerValue> = sequenceOf(
        DrawerValue.Closed,
        DrawerValue.Open
    )
) : PreviewParameterProvider<DrawerValue>

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(@PreviewParameter(HomeScreenProvider::class) state: DrawerValue) {
    HomeScreen(rememberDrawerState(initialValue = state)) {}
}