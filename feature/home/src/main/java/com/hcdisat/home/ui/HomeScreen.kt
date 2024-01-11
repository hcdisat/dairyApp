package com.hcdisat.home.ui

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
import androidx.compose.ui.res.stringResource
import com.hcdisat.core.ui.R
import com.hcdisat.home.model.DiaryState
import com.hcdisat.home.model.HomeEvent
import com.hcdisat.home.model.HomeEventAction
import com.hcdisat.home.model.isFiltered
import com.hcdisat.ui.components.AppAlertDialog
import com.hcdisat.ui.components.AppNavigationDrawer
import com.hcdisat.ui.components.AppScaffold
import com.hcdisat.ui.components.DialogEvent
import com.hcdisat.ui.components.NavigationDrawerEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    homeState: DiaryState,
    drawerState: DrawerState,
    topBarScrollBehavior: TopAppBarScrollBehavior? = null,
    onEvent: HomeEventAction,
) {
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
                    onEvent = {
                        when (this) {
                            is HomeEvent.MenuClicked -> HomeEvent.OpenDrawer.onEvent()
                            is HomeEvent.DiaryFilterEvent -> onEvent(this)
                            else -> Unit
                        }
                    },
                    scrollBehavior = topBarScrollBehavior,
                    isFiltered = homeState.isFiltered
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
                onEvent = onEvent
            )
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
                onEvent(HomeEvent.RemoveAll)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.addNestedScrollBehavior(behavior: TopAppBarScrollBehavior?) =
    behavior?.let { nestedScroll(it.nestedScrollConnection) } ?: this