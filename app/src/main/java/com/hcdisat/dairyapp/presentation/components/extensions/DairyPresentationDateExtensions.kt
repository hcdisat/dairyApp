package com.hcdisat.dairyapp.presentation.components.extensions

import com.hcdisat.dairyapp.presentation.components.model.DairyPresentationDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

fun Instant.toLocalDate(): LocalDate {
    return atZone(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDate.toPresentationDate(): DairyPresentationDate {
    requireNotNull(this.dayOfMonth)
    requireNotNull(this.dayOfWeek)
    requireNotNull(this.month)
    requireNotNull(this.year)

    return DairyPresentationDate(
        dayOfMonth = String.format("%02d", dayOfMonth),
        dayOfWeek = dayOfWeek.toString().take(3),
        month = month.toString().lowercase().replaceFirstChar { it.uppercase() },
        year = this.year.toString()
    )
}

fun Instant.toTimeString(format: String = "hh:mm a"): String {
    val formatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern(format, Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    return runCatching { formatter.format(this) }.getOrElse { "" }
}

fun Duration.toTimeString(format: String = "hh:mm a"): String =
    Instant.ofEpochMilli(this.inWholeMilliseconds).toTimeString(format)