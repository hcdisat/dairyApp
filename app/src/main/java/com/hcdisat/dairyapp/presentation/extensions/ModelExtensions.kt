package com.hcdisat.dairyapp.presentation.extensions

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.MutableDairyPresentationDate
import com.hcdisat.dairyapp.presentation.components.model.MutablePresentationDiary
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary

fun DomainDiary.toPresentationDiary() = PresentationDiary(
    id = id,
    title = title,
    description = description,
    time = date.toTimeString(),
    mood = Mood.valueOf(mood),
    images = images,
)

fun PresentationDiary.update(
    mutableDiaryScope: MutablePresentationDiary.() -> Unit
): PresentationDiary {
    val diary = MutablePresentationDiary(
        id = id,
        title = title,
        description = description,
        time = time,
        mood = mood,
        images = images.toMutableList(),
        date = date.toMutableDairyPresentationDate()
    )

    diary.mutableDiaryScope()
    return diary.toPresentationDiary()
}

fun MutableDairyPresentationDate.toDairyPresentationDate(): DairyPresentationDate =
    DairyPresentationDate(
        dayOfMonth = dayOfMonth,
        dayOfWeek = dayOfWeek,
        month = month,
        year = year
    )

fun DairyPresentationDate.toMutableDairyPresentationDate(): MutableDairyPresentationDate =
    MutableDairyPresentationDate(
        dayOfMonth = dayOfMonth,
        dayOfWeek = dayOfWeek,
        month = month,
        year = year
    )

private fun MutablePresentationDiary.toPresentationDiary(): PresentationDiary =
    PresentationDiary(
        id = id,
        title = title,
        description = description,
        time = time,
        mood = mood,
        images = images,
        date = date.toDairyPresentationDate()
    )