package com.hcdisat.dairyapp.feature_auth.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.dairyapp.feature_auth.ui.components.AuthenticationContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    onButtonClick: () -> Unit
) {
    Scaffold(
        content = {
            AuthenticationContent(loadingState = loadingState, onButtonClick = onButtonClick)
        }
    )
}

class AuthenticationScreenProvider(
    override val values: Sequence<Boolean> = sequenceOf(
        true,
        false
    )
) : PreviewParameterProvider<Boolean>

@Preview
@Composable
fun AuthenticationScreenPreview(
    @PreviewParameter(AuthenticationScreenProvider::class) loadingState: Boolean
) {
    AuthenticationScreen(loadingState = loadingState) {}
}