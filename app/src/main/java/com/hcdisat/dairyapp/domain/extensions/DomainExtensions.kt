package com.hcdisat.dairyapp.domain.extensions

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import io.realm.kotlin.internal.toDuration
import java.time.Instant

fun Diary.toDomainDiary() = DomainDiary(
    id = _id.toHexString(),
    ownerId = ownerId,
    title = title,
    description = description,
    date = Instant.ofEpochMilli(date.toDuration().inWholeMilliseconds),
    mood = mood,
    images = images.toList()
)