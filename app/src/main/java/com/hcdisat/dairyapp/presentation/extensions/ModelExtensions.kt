package com.hcdisat.dairyapp.presentation.extensions

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.MutablePresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import java.time.LocalDateTime

fun DomainDiary.toPresentationDiary() = PresentationDiary(
    id = id,
    title = title,
    description = description,
    dateTime = date,
    mood = Mood.valueOf(mood),
    images = images,
)

fun LocalDateTime.toPresentationDate(): DairyPresentationDate {
    return DairyPresentationDate(
        dayOfMonth = String.format("%02d", dayOfMonth),
        dayOfWeek = dayOfWeek.toString().take(3),
        month = month.toString(),
        year = year.toString()
    )
}

fun PresentationDiary.update(
    mutableDiaryScope: MutablePresentationDiary.() -> Unit
): PresentationDiary {
    val diary = MutablePresentationDiary(
        id = id,
        title = title,
        description = description,
        mood = mood,
        images = images.toMutableList(),
        date = dateTime
    )

    diary.mutableDiaryScope()
    return diary.toPresentationDiary()
}

private fun MutablePresentationDiary.toPresentationDiary(): PresentationDiary =
    PresentationDiary(
        id = id,
        title = title,
        description = description,
        mood = mood,
        images = images,
        dateTime = date
    )