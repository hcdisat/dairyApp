package com.hcdisat.dairyapp.feature_home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.DiaryResult
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction
import com.hcdisat.dairyapp.presentation.components.AppNavigationDrawer
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.NavigationDrawerEvent
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

@Composable
fun HomeScreen(
    drawerState: DrawerState,
    diaryResult: DiaryResult,
    onEvent: HomeEventAction,
) {
    AppNavigationDrawer(
        drawerState = drawerState,
        onEvent = {
            when (this) {
                NavigationDrawerEvent.Logout -> HomeEvent.Logout.onEvent()
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
            HomeContent(
                homeState = diaryResult,
                paddingValues = this,
                onClick = {}
            )
        }
    }
}

data class HomeParameters(
    val drawerValue: DrawerValue,
    val diaryResult: DiaryResult,
)

private fun getLoadedState(): Map<DairyPresentationDate, List<PresentationDiary>> {
    val diary = PresentationDiary(
        title = "My new Diary",
        description = "My new Diary",
        id = "123",
        time = "04:30 PM",
        mood = Mood.Happy
    )

    val date = DairyPresentationDate(
        dayOfMonth = "16",
        dayOfWeek = "SUN",
        month = "December",
        year = "2023"
    )

    return mapOf(date to listOf(diary))
}

class HomeScreenProvider : PreviewParameterProvider<HomeParameters> {
    override val values: Sequence<HomeParameters>
        get() {
            return sequenceOf(
                // Drawer Open
                HomeParameters(
                    drawerValue = DrawerValue.Open,
                    diaryResult = DiaryResult.Loading
                ),
                // Loading
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryResult.Loading
                ),
                // Error
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryResult.Error(Exception())
                ),
                // With Content
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryResult.Loaded(getLoadedState())
                )
            )
        }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(@PreviewParameter(HomeScreenProvider::class) state: HomeParameters) {
    HomeScreen(
        DrawerState(state.drawerValue),
        diaryResult = state.diaryResult
    ) {}
}