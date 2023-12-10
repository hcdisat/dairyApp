package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.dairyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onBackPressed: (() -> Unit)? = null,
    onAddDatePressed: (() -> Unit)? = null,
    onMoreOptionsPressed: (() -> Unit)? = null
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
            IconButton(onClick = { onMoreOptionsPressed?.invoke() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.select_date_cd)
                )
            }
        }
    )
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