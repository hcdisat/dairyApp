package com.hcdisat.dairyapp.feature_home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction
import com.hcdisat.dairyapp.presentation.components.AppAlertDialog
import com.hcdisat.dairyapp.presentation.components.AppNavigationDrawer
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.DialogEvent
import com.hcdisat.dairyapp.presentation.components.NavigationDrawerEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    topBarScrollBehavior: TopAppBarScrollBehavior? = null,
    onEvent: HomeEventAction,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeState by viewModel.homeState.collectAsStateWithLifecycle(
        lifecycle = LocalLifecycleOwner.current.lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )

    var isLogoutDialogDismissed by remember { mutableStateOf(true) }
    var isRemoveAllDialogDismissed by remember { mutableStateOf(true) }

    AppNavigationDrawer(
        drawerState = drawerState,
        onEvent = {
            when (this) {
                NavigationDrawerEvent.Logout -> isLogoutDialogDismissed = false
                NavigationDrawerEvent.DeleteAllEntries -> isRemoveAllDialogDismissed = false
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
                FloatingActionButton(onClick = { HomeEvent.AddNewEntry.onEvent() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.add_new_note_cd)
                    )
                }
            }
        ) {
            HomeContent(
                homeState = homeState,
                paddingValues = this,
            ) {
                when (this) {
                    is HomeEvent.HideGallery -> viewModel.hideGalleryImages(diary)
                    is HomeEvent.ShowGallery -> viewModel.showGalleryImages(diary)
                    is HomeEvent.LoadGallery -> viewModel.loadImageGallery(diary)
                    else -> onEvent(this)
                }
            }
        }
    }

    AppAlertDialog(
        isDismissed = isLogoutDialogDismissed,
        message = stringResource(R.string.logout_dialog_message),
        title = stringResource(R.string.login_out_dialog_title),
        confirmButtonText = stringResource(R.string.yes_btn),
        dismissButtonText = stringResource(R.string.no_btn),
        onEvent = {
            isLogoutDialogDismissed = true
            if (it == DialogEvent.POSITIVE) {
                viewModel.logout()
                HomeEvent.Logout.onEvent()
            }
        }
    )

    AppAlertDialog(
        isDismissed = isRemoveAllDialogDismissed,
        message = stringResource(R.string.remove_all_dialog_message),
        title = stringResource(R.string.remove_all_dialog_title),
        confirmButtonText = stringResource(R.string.yes_btn),
        dismissButtonText = stringResource(R.string.btn_cancel),
        onEvent = {
            isRemoveAllDialogDismissed = true
            if (it == DialogEvent.POSITIVE) {
                viewModel.removeAllDiaries()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.addNestedScrollBehavior(behavior: TopAppBarScrollBehavior?) =
    behavior?.let { nestedScroll(it.nestedScrollConnection) } ?: this