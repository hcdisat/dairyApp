package com.hcdisat.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: (@Composable () -> Unit)? = null,
    floatingAction: @Composable () -> Unit = {},
    content: @Composable PaddingValues.() -> Unit
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier,
        floatingActionButton = floatingAction,
        bottomBar = { bottomBar?.let { BottomAppBar { it() } } },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { paddingValues ->
            messageBarState?.let { messageBarState ->
                ContentWithMessageBar(messageBarState = messageBarState) {
                    paddingValues.content()
                }
            } ?: paddingValues.content()
        }
    )
}

@Preview
@Composable
private fun AppScaffoldPreview() {
    AppScaffold { Logout {} }
}