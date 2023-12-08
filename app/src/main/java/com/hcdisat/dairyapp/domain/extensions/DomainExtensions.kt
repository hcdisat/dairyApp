package com.hcdisat.dairyapp.domain.extensions

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import io.realm.kotlin.internal.toDuration
import java.time.Instant
import java.time.LocalDate

fun Diary.toDomainDiary() = DomainDiary(
    id = _id.toString(),
    ownerId = ownerId,
    title = title,
    description = description,
    date = Instant.ofEpochMilli(date.toDuration().inWholeMilliseconds),
    mood = mood,
    images = images.toList()
)

fun Map<LocalDate, List<Diary>>.toDiaryResponse() = map { (key, value) ->
    key to value.map { it.toDomainDiary() }
}.toMap()