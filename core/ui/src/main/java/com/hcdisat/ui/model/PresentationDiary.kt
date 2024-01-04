package com.hcdisat.ui.model

import com.hcdisat.ui.extensions.getFormattedTime
import com.hcdisat.ui.extensions.getInlineDateTime
import java.time.LocalDateTime

data class DiaryHeaderPresentation(val mood: Mood, val time: String)

data class PresentationDiary(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val mood: Mood = Mood.Neutral,
    val images: List<String> = listOf()
)

data class MutablePresentationDiary(
    val id: String = "",
    var title: String = "",
    var description: String = "",
    var dateTime: LocalDateTime = LocalDateTime.now(),
    var mood: Mood = Mood.Neutral,
    val images: MutableList<String> = mutableListOf()
)

val PresentationDiary.formattedDateTime get() = dateTime.getInlineDateTime()

val PresentationDiary.time: String get() = dateTime.getFormattedTime()