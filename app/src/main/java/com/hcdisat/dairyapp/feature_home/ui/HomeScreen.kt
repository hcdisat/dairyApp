package com.hcdisat.dairyapp.feature_home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.DiaryState
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction
import com.hcdisat.dairyapp.presentation.components.AppNavigationDrawer
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.NavigationDrawerEvent
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    diaryResult: DiaryState,
    topBarScrollBehavior: TopAppBarScrollBehavior? = null,
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
            modifier = modifier
                .addNestedScrollBehavior(topBarScrollBehavior)
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                HomeTopBar(
                    onEvent = { HomeEvent.OpenDrawer.onEvent() },
                    scrollBehavior = topBarScrollBehavior
                )
            },
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

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.addNestedScrollBehavior(behavior: TopAppBarScrollBehavior?) =
    behavior?.let { nestedScroll(it.nestedScrollConnection) } ?: this

data class HomeParameters(
    val drawerValue: DrawerValue,
    val diaryResult: DiaryState,
)

private fun getLoadedState(): Map<DairyPresentationDate, List<PresentationDiary>> {
    val diary = PresentationDiary(
        title = "My new Diary",
        description = "My new Diary",
        id = "123",
        time = "04:30 PM",
        mood = Mood.Happy,
        images = listOf(
            "https://najlepszamuzyka.pl/43036-large_default/korn-issues-2lp-mov-edition-180-gram-pressing.jpg",
            "https://cdn.afew-store.com/assets/40/402366/1200/adidas-originals-x-korn-campus-00s-cblack-ftwwht-ricpur-ig0792-footwear%20%3E%20sneaker-packshots-30.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSLcjoT9_OPx7rvI4JcNXd9XR0hPjKGBzsOuXpdGJiiWYfVjmdDwDH4BOP18CCfkLcL65A&usqp=CAU",
            "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Korn-Korn.jpg/220px-Korn-Korn.jpg",
            "https://w0.peakpx.com/wallpaper/858/333/HD-wallpaper-korn-rock-band.jpg",
            "https://w0.peakpx.com/wallpaper/858/333/HD-wallpaper-korn-rock-band.jpg",
            "https://w0.peakpx.com/wallpaper/858/333/HD-wallpaper-korn-rock-band.jpg",
        )
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
                    diaryResult = DiaryState.Loading
                ),
                // Loading
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryState.Loading
                ),
                // Error
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryState.Error(Exception())
                ),
                // With Content
                HomeParameters(
                    drawerValue = DrawerValue.Closed,
                    diaryResult = DiaryState.Loaded(getLoadedState())
                )
            )
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview(@PreviewParameter(HomeScreenProvider::class) state: HomeParameters) {
    HomeScreen(
        drawerState = DrawerState(state.drawerValue),
        diaryResult = state.diaryResult
    ) {}
}