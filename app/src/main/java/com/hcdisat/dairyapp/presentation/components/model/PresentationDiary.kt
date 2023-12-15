package com.hcdisat.dairyapp.presentation.components.model

import com.hcdisat.dairyapp.presentation.extensions.toPresentationDate
import com.hcdisat.dairyapp.presentation.extensions.toTimeString
import java.time.Instant
import java.time.LocalDate

data class DiaryHeaderPresentation(val mood: Mood, val time: String)

data class PresentationDiary(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = Instant.now().toTimeString(),
    val date: DairyPresentationDate = LocalDate.now().toPresentationDate(),
    val mood: Mood = Mood.Neutral,
    val images: List<String> = listOf()
)

val PresentationDiary.dateTime
    get() =
        "${date.inlineDate}, $time".takeIf { date.inlineDate.isNotBlank() }

data class MutablePresentationDiary(
    val id: String = "",
    var title: String = "",
    var description: String = "",
    var time: String = "",
    var date: MutableDairyPresentationDate = MutableDairyPresentationDate(),
    var mood: Mood = Mood.Neutral,
    val images: MutableList<String> = mutableListOf()
)

data class MutableDairyPresentationDate(
    var dayOfMonth: String = "",
    var dayOfWeek: String = "",
    var month: String = "",
    var year: String = ""
)