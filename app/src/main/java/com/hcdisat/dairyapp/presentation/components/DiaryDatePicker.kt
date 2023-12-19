package com.hcdisat.dairyapp.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.dairyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePicker(
    modifier: Modifier = Modifier,
    selectedTimeInMillis: Long? = null,
    showDatePicker: Boolean = false,
    onEvent: DatePickerEvents.() -> Unit = {}
) {
    if (!showDatePicker) return

    val state = rememberDatePickerState(initialSelectedDateMillis = selectedTimeInMillis)
    DatePickerDialog(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = {
            DatePickerEvents.OnDismissed.onEvent()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let {
                        DatePickerEvents.DateSelected(it).onEvent()
                    }
                }
            ) {
                Text(text = stringResource(R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { DatePickerEvents.OnDismissed.onEvent() }) {
                Text(text = stringResource(R.string.btn_cancel))
            }
        },
    ) {
        DatePicker(state = state)
    }
}

sealed interface DatePickerEvents {
    data class DateSelected(val dateInUtcMillis: Long) : DatePickerEvents
    data object OnDismissed : DatePickerEvents
}

@Preview
@Composable
fun DiaryDatePickerPreview() {
    DiaryDatePicker(showDatePicker = true) {}
}