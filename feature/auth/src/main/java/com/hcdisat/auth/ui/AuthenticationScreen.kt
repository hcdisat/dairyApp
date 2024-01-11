package com.hcdisat.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.auth.AuthUIEvents
import com.hcdisat.auth.AuthenticationState
import com.hcdisat.auth.ui.components.AuthenticationContent
import com.hcdisat.common.settings.Constants.CLIENT_ID
import com.hcdisat.ui.components.AppScaffold
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.delay

@Composable
internal fun AuthenticationScreen(
    state: AuthenticationState,
    onEvent: (AuthUIEvents) -> Unit = {},
) {
    val messageBarState = rememberMessageBarState()
    val oneTapState = rememberOneTapSignInState()
    val (sessionState, loadingState) = state

    AppScaffold(
        modifier = Modifier,
        messageBarState = messageBarState,
        topBar = {},
        content = {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClick = {
                    onEvent(AuthUIEvents.LoadingStatus(true))
                    oneTapState.open()
                }
            )
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { token -> onEvent(AuthUIEvents.OnTokenReceived(token)) },
        onDialogDismissed = {
            messageBarState.addError(Exception(it))
            onEvent(AuthUIEvents.LoadingStatus(false))
        }
    )

    LaunchedEffect(key1 = sessionState) {
        when (sessionState) {
            is AccountSessionState.LoggedOut -> Unit

            AccountSessionState.LoggedIn -> {
                messageBarState.addSuccess("Successfully Authenticated!")
                delay(500)
                onEvent(AuthUIEvents.SuccessLogin)
            }

            is AccountSessionState.Error -> {
                messageBarState.addError(Exception("Something went wrong please try again"))
            }
        }
    }
}