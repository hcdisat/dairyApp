package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.core.ui.R
import com.hcdisat.ui.components.AppAlertDialog
import com.hcdisat.ui.components.DialogEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    isEdit: Boolean = false,
    diaryTitle: String? = null,
    onBackPressed: (() -> Unit)? = null,
    onAddDatePressed: (() -> Unit)? = null,
    onDeletePressed: (() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed?.invoke() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_navigation_icon_cd)
                )
            }
        },
        actions = {
            IconButton(onClick = { onAddDatePressed?.invoke() }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date_cd)
                )
            }
            if (isEdit) {
                DeleteDiaryAction(onDeleteClicked = onDeletePressed, diaryTitle = diaryTitle)
            }
        }
    )
}

@Composable
fun DeleteDiaryAction(diaryTitle: String?, onDeleteClicked: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var confirmedDialog by remember { mutableStateOf(true) }
    val message = """
        ${stringResource(R.string.confirm_delete_message)} "${diaryTitle.orEmpty()}" 
        """.trimIndent()

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = "Delete") },
            onClick = {
                expanded = false
                confirmedDialog = false
            }
        )
    }

    AppAlertDialog(
        isDismissed = confirmedDialog,
        message = message,
        title = stringResource(R.string.remove_entry_title),
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_diary_action_cd)
            )
        },
        onEvent = {
            confirmedDialog = when (it) {
                DialogEvent.POSITIVE -> {
                    onDeleteClicked()
                    true
                }

                DialogEvent.NEGATIVE -> {
                    true
                }
            }
        }
    )

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.select_date_cd)
        )
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, device = Devices.PIXEL_TABLET)
@Composable
private fun WriteTopBarPreview(@PreviewParameter(WriteTopBarProvider::class) state: WriteState) {
    WriteTopBar(title = state.title, subtitle = state.subtitle)
}

data class WriteState(
    val title: String = "",
    val subtitle: String = "",
)

class WriteTopBarProvider : PreviewParameterProvider<WriteState> {
    override val values: Sequence<WriteState>
        get() = sequenceOf(
            WriteState(
                title = "Happy",
                subtitle = "JAN 10 2023, 10:00 AM"
            )
        )
}