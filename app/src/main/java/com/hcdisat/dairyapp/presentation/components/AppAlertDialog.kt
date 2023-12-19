package com.hcdisat.dairyapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.dairyapp.R

enum class DialogEvent { POSITIVE, NEGATIVE }

@Composable
fun AppAlertDialog(
    isDismissed: Boolean,
    message: String,
    title: String,
    confirmButtonText: String = stringResource(R.string.btn_accept),
    dismissButtonText: String = stringResource(R.string.btn_cancel),
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null
        )
    },
    onEvent: (DialogEvent) -> Unit = {},
) {
    if (isDismissed) return

    AlertDialog(
        icon = icon,
        title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyLarge) },
        onDismissRequest = { onEvent(DialogEvent.POSITIVE) },
        confirmButton = {
            Button(onClick = { onEvent(DialogEvent.POSITIVE) }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(DialogEvent.NEGATIVE) }) {
                Text(text = dismissButtonText)
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AppAlertDialogPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppAlertDialog(
            isDismissed = false,
            message = "Some MessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessage",
            title = "ALERT"
        )
    }
}