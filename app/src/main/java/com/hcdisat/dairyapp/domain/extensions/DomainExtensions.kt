package com.hcdisat.dairyapp.domain.extensions

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.core.extensions.toLocalDateTime
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun Diary.toDomainDiary() = DomainDiary(
    id = _id.toHexString(),
    ownerId = ownerId,
    title = title,
    description = description,
    date = date.toLocalDateTime(),
    mood = mood,
    images = images.toList()
)

fun Instant.toRealmInstant(): RealmInstant {
    return if (epochSecond >= 0) {
        RealmInstant.from(epochSecond, nano)
    } else {
        RealmInstant.from(
            epochSecond + 1,
            1_000_000 + nano
        )
    }
}