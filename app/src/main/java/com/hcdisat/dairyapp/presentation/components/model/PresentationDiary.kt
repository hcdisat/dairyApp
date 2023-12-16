package com.hcdisat.dairyapp.presentation.components.model

import com.hcdisat.dairyapp.presentation.extensions.getFormattedTime
import com.hcdisat.dairyapp.presentation.extensions.getInlineDate
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
    var date: LocalDateTime = LocalDateTime.now(),
    var mood: Mood = Mood.Neutral,
    val images: MutableList<String> = mutableListOf()
)

val PresentationDiary.formattedDateTime get() = dateTime.getInlineDate()

val PresentationDiary.time: String get() = dateTime.getFormattedTime()