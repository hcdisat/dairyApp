package com.hcdisat.dairyapp.abstraction.domain.model

import java.time.LocalDateTime

data class DomainDiary(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<String> = listOf(),
    val date: LocalDateTime = LocalDateTime.now(),
    val mood: String = "Neutral",
)