package com.hcdisat.home

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hcdisat.abstraction.navigation.RouteApi
import com.hcdisat.abstraction.navigation.events.HomeNavigationEvent
import com.hcdisat.common.settings.NavigationConstants
import com.hcdisat.home.model.DiaryFilterAction
import com.hcdisat.home.model.HomeEvent
import com.hcdisat.home.ui.HomeScreen
import com.hcdisat.home.ui.HomeViewModel
import com.hcdisat.ui.components.DatePickerEvents
import com.hcdisat.ui.components.DiaryDatePicker
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

interface HomeRoute : RouteApi<HomeNavigationEvent> {
    fun passDiaryId(entryId: String?): String
}

class HomeRouteImpl @Inject constructor() : HomeRoute {
    override val route: String = NavigationConstants.HOME_ROUTE

    override fun passDiaryId(entryId: String?): String = entryId?.let {
        "write_screen?${NavigationConstants.WRITE_ARGUMENT}=$it"
    } ?: NavigationConstants.WRITE_ROUTE

    @OptIn(ExperimentalMaterial3Api::class)
    override fun register(
        navGraphBuilder: NavGraphBuilder,
        onEvent: (HomeNavigationEvent) -> Unit
    ) {
        navGraphBuilder.composable(route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val homeState by viewModel.homeState.collectAsStateWithLifecycle(
                lifecycle = LocalLifecycleOwner.current.lifecycle,
                minActiveState = Lifecycle.State.STARTED
            )

            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

            var shouldOpenDatePicker by remember { mutableStateOf(false) }

            HomeScreen(
                homeState = homeState,
                drawerState = drawerState,
                topBarScrollBehavior = scrollBehavior
            ) {
                when (this) {
                    is HomeEvent.AddNewEntry -> onEvent(HomeNavigationEvent.AddNewEntry)
                    is HomeEvent.EditEntry -> onEvent(HomeNavigationEvent.EditEntry(entryId))
                    is HomeEvent.OpenDrawer -> scope.launch { drawerState.open() }
                    is HomeEvent.HideGallery -> viewModel.hideGalleryImages(diary)
                    is HomeEvent.ShowGallery -> viewModel.showGalleryImages(diary)
                    is HomeEvent.LoadGallery -> viewModel.loadImageGallery(diary)
                    is HomeEvent.RemoveAll -> viewModel.removeAllDiaries()
                    is HomeEvent.MenuClicked -> Unit
                    is HomeEvent.Logout -> {
                        viewModel.logout()
                        onEvent(HomeNavigationEvent.Logout)
                    }

                    is HomeEvent.DiaryFilterEvent -> {
                        if (action == DiaryFilterAction.AttachFilter)
                            shouldOpenDatePicker = true
                        else {
                            shouldOpenDatePicker = false
                            viewModel.removeFilter()
                        }
                    }

                }
            }

            DiaryDatePicker(
                showDatePicker = shouldOpenDatePicker,
                selectedTimeInMillis = Instant.now().toEpochMilli()
            ) {
                shouldOpenDatePicker = when (this) {
                    is DatePickerEvents.DateSelected -> {
                        viewModel.filterDiaries(dateInUtcMillis)
                        false
                    }

                    DatePickerEvents.OnDismissed -> {
                        false
                    }
                }
            }
        }
    }
}