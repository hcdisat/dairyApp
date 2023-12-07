package com.hcdisat.dairyapp.presentation.components.model

data class DiaryHeaderPresentation(val mood: Mood, val time: String)

data class PresentationDiary(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val mood: Mood = Mood.Neutral,
    val images: List<String> = listOf()
)