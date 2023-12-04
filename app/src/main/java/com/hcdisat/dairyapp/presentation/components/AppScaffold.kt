package com.hcdisat.dairyapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
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
    floatingAction: @Composable () -> Unit = {},
    content: @Composable PaddingValues.() -> Unit
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        floatingActionButton = floatingAction,
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
fun AppScaffoldPreview() {
    AppScaffold { Logout {} }
}