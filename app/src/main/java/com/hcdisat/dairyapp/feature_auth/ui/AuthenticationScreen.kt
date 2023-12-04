package com.hcdisat.dairyapp.feature_auth.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hcdisat.dairyapp.abstraction.networking.AccountSessionState
import com.hcdisat.dairyapp.feature_auth.ui.components.AuthenticationContent
import com.hcdisat.dairyapp.settings.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticationState: AuthenticationState,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClick: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    val (sessionState, loadingState) = authenticationState
    Scaffold(
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClick = {
                        oneTapState.open()
                        onButtonClick()
                    }
                )
            }
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = onTokenIdReceived,
        onDialogDismissed = onDialogDismissed
    )

    LaunchedEffect(key1 = sessionState) {
        when (sessionState) {
            AccountSessionState.LOGGED_OUT -> Unit

            AccountSessionState.LOGGED_IN -> {
                messageBarState.addSuccess("Successfully Authenticated!")
                delay(600)
                onLoginSuccess()
            }

            AccountSessionState.ERROR -> {
                messageBarState.addError(Exception("Something went wrong please try again"))
            }
        }
    }
}