package com.hcdisat.ui.extensions

import com.hcdisat.abstraction.domain.model.DomainDiary
import com.hcdisat.ui.model.DairyPresentationDate
import com.hcdisat.ui.model.Mood
import com.hcdisat.ui.model.PresentationDiary
import java.time.LocalDateTime
import java.time.ZoneId

fun DomainDiary.toPresentationDiary() = PresentationDiary(
    id = id,
    title = title,
    description = description,
    dateTime = date,
    mood = Mood.valueOf(mood),
    images = images,
)

fun PresentationDiary.toDomain() = DomainDiary(
    id = id,
    title = title,
    description = description,
    date = dateTime,
    mood = mood.name,
    images = images
)

fun LocalDateTime.toPresentationDate(): DairyPresentationDate {
    return DairyPresentationDate(
        dayOfMonth = String.format("%02d", dayOfMonth),
        dayOfWeek = dayOfWeek.toString().take(3),
        month = month.toString(),
        year = year.toString()
    )
}

fun LocalDateTime.toMillis(): Long = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()