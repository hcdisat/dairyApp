package com.hcdisat.dairyapp.feature_home.domain

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.toTimeString

fun DomainDiary.toPresentationDiary() = PresentationDiary(
    id = id,
    title = title,
    description = description,
    time = date.toTimeString(),
    mood = Mood.valueOf(mood),
    images = images
)