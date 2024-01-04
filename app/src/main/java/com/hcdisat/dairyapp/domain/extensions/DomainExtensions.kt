package com.hcdisat.dairyapp.domain.extensions

import com.hcdisat.abstraction.domain.model.DomainDiary
import com.hcdisat.common.extensions.toLocalDateTime
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import org.mongodb.kbson.ObjectId
import java.time.Instant
import java.time.ZoneId

fun Diary.toDomainDiary() = DomainDiary(
    id = _id.toHexString(),
    ownerId = ownerId,
    title = title,
    description = description,
    date = date.toLocalDateTime(),
    mood = mood,
    images = images.toList()
)

fun DomainDiary.toDiary(): Result<Diary> = runCatching {
    val diary = Diary()
    if (id.isNotBlank()) {
        diary._id = ObjectId.invoke(id)
    }

    diary.ownerId = ownerId
    diary.title = title
    diary.description = description
    diary.date = date.atZone(ZoneId.systemDefault()).toInstant().toRealmInstant()
    diary.mood = mood
    diary.images = realmListOf(*images.toTypedArray())

    diary
}

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