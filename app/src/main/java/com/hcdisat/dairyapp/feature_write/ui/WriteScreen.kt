package com.hcdisat.dairyapp.feature_write.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hcdisat.dairyapp.presentation.components.AppScaffold
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.dateTime

@Composable
fun WriteScreen(
    diary: PresentationDiary? = null,
    onDelete: PresentationDiary.() -> Unit,
    onBackPressed: () -> Unit
) {
    AppScaffold(
        topBar = {
            WriteTopBar(
                title = diary?.mood?.name ?: "",
                subtitle = diary?.dateTime ?: "",
                diaryTitle = diary?.title,
                onBackPressed = onBackPressed,
                isEdit = diary != null,
                onDeletePressed = { diary?.onDelete() }
            )
        }
    ) {

    }
}

@Preview(showSystemUi = true)
@Composable
private fun WriteScreenPreview() {
    MaterialTheme {
        WriteScreen(
            diary = WriteScreenDataProvider.diary,
            onDelete = {}
        ) {}
    }
}

object WriteScreenDataProvider {
    val diary = PresentationDiary().copy(
        time = "04:00 PM",
        date = DairyPresentationDate(
            dayOfMonth = "16",
            dayOfWeek = "SAT",
            month = "FEB",
            year = "2023"
        )
    )
}