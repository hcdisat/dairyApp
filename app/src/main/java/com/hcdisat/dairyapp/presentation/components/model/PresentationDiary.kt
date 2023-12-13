package com.hcdisat.dairyapp.presentation.components.model

data class DiaryHeaderPresentation(val mood: Mood, val time: String)

data class PresentationDiary(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val date: DairyPresentationDate = DairyPresentationDate(),
    val mood: Mood = Mood.Neutral,
    val images: List<String> = listOf()
)

val PresentationDiary.dateTime get() = "${date.inlineDate}, $time"

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