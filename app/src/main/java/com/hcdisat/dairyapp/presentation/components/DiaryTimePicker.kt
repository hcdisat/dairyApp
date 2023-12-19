package com.hcdisat.dairyapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DiaryTimePicker(
    showTimePicker: Boolean = true,
    is24Hour: Boolean = false,
    atHour: Int = 0,
    atMinute: Int = 0,
    onEvents: TimePickerEvents.() -> Unit = {}
) {
    if (!showTimePicker) return

    val state = rememberTimePickerState(
        is24Hour = is24Hour,
        initialHour = atHour,
        initialMinute = atMinute
    )

    DatePickerDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = { TimePickerEvents.OnDismissed.onEvents() },
        confirmButton = {
            TextButton(
                onClick = {
                    val (hour, minute) = state
                    TimePickerEvents.TimeSelected(hour, minute).onEvents()
                }
            ) {
                Text(text = stringResource(id = R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { TimePickerEvents.OnDismissed.onEvents() }) {
                Text(text = stringResource(id = R.string.btn_cancel))
            }
        }
    ) {
        TimePicker(
            state = state,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private operator fun TimePickerState.component1(): Int = hour

@OptIn(ExperimentalMaterial3Api::class)
private operator fun TimePickerState.component2(): Int = minute

sealed interface TimePickerEvents {
    data class TimeSelected(val hour: Int, val minute: Int) : TimePickerEvents
    data object OnDismissed : TimePickerEvents
}