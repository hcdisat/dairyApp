package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_write.model.WriteEntryEvents
import com.hcdisat.dairyapp.presentation.components.MoodPager
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

@Preview(showSystemUi = true)
@Composable
fun WriteContent(
    diary: PresentationDiary = PresentationDiary(),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onEvent: WriteEntryEvents.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = paddingValues.calculateBottomPadding()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val scrollState = rememberScrollState()
        val placeHolderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .38f)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
        ) {
            MoodPager(modifier = Modifier.size(120.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = diary.title,
                placeholder = { Text(text = stringResource(R.string.entry_title_placeholder)) },
                onValueChange = { onEvent(WriteEntryEvents.OnTitleChanged(it)) },
                colors = textFieldColors(placeHolderColor = placeHolderColor),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {}),
                maxLines = 1,
                singleLine = true
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = diary.description,
                placeholder = { Text(text = stringResource(R.string.entry_description_placeholder)) },
                onValueChange = { onEvent(WriteEntryEvents.OnDescriptionChanged(it)) },
                colors = textFieldColors(placeHolderColor = placeHolderColor),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {}),
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(54.dp),
                onClick = {},
                shape = Shapes().small
            ) {
                Text(text = stringResource(R.string.save_btn))
            }
        }
    }
}

@Composable
private fun textFieldColors(placeHolderColor: Color) = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Unspecified,

    focusedIndicatorColor = Color.Unspecified,
    unfocusedIndicatorColor = Color.Unspecified,
    disabledIndicatorColor = Color.Unspecified,

    disabledPlaceholderColor = placeHolderColor,
    focusedPlaceholderColor = placeHolderColor,
    unfocusedPlaceholderColor = placeHolderColor,
)