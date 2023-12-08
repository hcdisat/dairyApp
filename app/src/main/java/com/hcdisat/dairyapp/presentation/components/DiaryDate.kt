package com.hcdisat.dairyapp.presentation.components

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
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDate
import java.time.LocalDate

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
                    color = MaterialTheme.typography.bodySmall.color.copy(alpha = .38f)
                )
            )
        }
    }
}

private data class DiaryDateProvider(
    override val values: Sequence<LocalDate> = sequenceOf(
        LocalDate.now(),
        LocalDate.now()
            .plusYears(1)
            .minusMonths(1)
            .minusDays(10)
    )
) : PreviewParameterProvider<LocalDate>

@Preview(showBackground = true)
@Composable
private fun DiaryDatePrev(@PreviewParameter(DiaryDateProvider::class) date: LocalDate) {
    DiaryDate(date = date.toPresentationDate())
}