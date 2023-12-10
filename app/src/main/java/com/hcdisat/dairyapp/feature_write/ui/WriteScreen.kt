package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.dairyapp.presentation.components.AppScaffold

@Composable
fun WriteScreen(
    onBackPressed: () -> Unit
) {
    AppScaffold(
        topBar = {
            WriteTopBar(
                title = "Happy",
                subtitle = "JAN 10 2023, 10:00 AM",
                onBackPressed = onBackPressed
            )
        }
    ) {

    }
}

@Preview(showSystemUi = true)
@Composable
private fun WriteScreenPreview() {
    MaterialTheme {
        WriteScreen {

        }
    }
}