package com.hcdisat.dairyapp.presentation.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TIME_FORMAT = "hh:mm a"

fun LocalDateTime.getInlineDate(): String =
    "${dayOfWeek.toString().take(3)} $month $dayOfMonth $year"

fun LocalDateTime.getInlineDateTime(): String =
    "${getInlineDate()}, ${getFormattedTime()}"

fun LocalDateTime.getFormattedTime(format: String = TIME_FORMAT): String = runCatching {
    val formatter = DateTimeFormatter.ofPattern(format)
    format(formatter)
}.getOrElse { "" }