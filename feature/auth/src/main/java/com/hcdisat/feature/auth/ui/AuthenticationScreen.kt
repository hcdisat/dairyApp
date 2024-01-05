package com.hcdisat.feature.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.hcdisat.abstraction.networking.AccountSessionState
import com.hcdisat.common.settings.Constants.CLIENT_ID
import com.hcdisat.feature.auth.ui.components.AuthenticationContent
import com.hcdisat.ui.components.AppScaffold
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.delay

@Composable
fun AuthenticationScreen(
    onLoginSuccess: () -> Unit
) {
    val messageBarState = rememberMessageBarState()
    val oneTapState = rememberOneTapSignInState()

    val viewModel: AuthenticationViewModel = hiltViewModel()
    val state = viewModel.userSessionState.collectAsState().value
    val (sessionState, loadingState) = state

    AppScaffold(
        modifier = Modifier,
        messageBarState = messageBarState,
        topBar = {},
        content = {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClick = {
                    oneTapState.open()
                    viewModel.setLoading(true)
                }
            )
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { token -> viewModel.signInWithAtlas(token) },
        onDialogDismissed = {
            messageBarState.addError(Exception(it))
            viewModel.setLoading(false)
        }
    )

    LaunchedEffect(key1 = sessionState) {
        when (sessionState) {
            is AccountSessionState.LoggedOut -> Unit

            AccountSessionState.LoggedIn -> {
                messageBarState.addSuccess("Successfully Authenticated!")
                delay(600)
                onLoginSuccess()
            }

            is AccountSessionState.Error -> {
                messageBarState.addError(Exception("Something went wrong please try again"))
            }
        }
    }
}