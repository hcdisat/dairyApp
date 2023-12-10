package com.hcdisat.dairyapp.presentation.components.model

data class DairyPresentationDate(
    val dayOfMonth: String = "",
    val dayOfWeek: String = "",
    val month: String = "",
    val year: String = ""
)

val DairyPresentationDate.date
    get() =
        "$dayOfWeek $month $dayOfMonth $year"