package com.hcdisat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hcdisat.core.ui.R
import com.hcdisat.ui.extensions.toPresentationDate
import com.hcdisat.ui.model.DairyPresentationDate
import java.time.LocalDateTime

@Composable
fun DiaryDate(
    modifier: Modifier = Modifier,
    date: DairyPresentationDate
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.diary_date_spacing)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = date.dayOfMonth,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = date.dayOfWeek,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }

        Column {
            Text(
                text = date.month,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = date.year,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .38f)
                )
            )
        }
    }
}

private data class DiaryDateProvider(
    override val values: Sequence<LocalDateTime> = sequenceOf(
        LocalDateTime.now(),
        LocalDateTime.now()
            .plusYears(1)
            .minusMonths(1)
            .minusDays(10)
    )
) : PreviewParameterProvider<LocalDateTime>

@Preview(showBackground = true)
@Composable
private fun DiaryDatePrev(@PreviewParameter(DiaryDateProvider::class) date: LocalDateTime) {
    DiaryDate(date = date.toPresentationDate())
}