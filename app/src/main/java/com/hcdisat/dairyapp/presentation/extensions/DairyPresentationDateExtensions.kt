package com.hcdisat.dairyapp.presentation.extensions

import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toPresentationDate(): DairyPresentationDate {
    requireNotNull(dayOfWeek)
    requireNotNull(month)

    return DairyPresentationDate(
        dayOfMonth = String.format("%02d", dayOfMonth),
        dayOfWeek = dayOfWeek.toString().take(3),
        month = month.toString().lowercase().replaceFirstChar { it.uppercase() },
        year = year.toString()
    )
}

fun Instant.toTimeString(format: String = "hh:mm a"): String {
    val formatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern(format, Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    return runCatching { formatter.format(this) }.getOrElse { "" }
}