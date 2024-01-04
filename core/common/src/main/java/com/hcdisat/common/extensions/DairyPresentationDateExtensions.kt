package com.hcdisat.common.extensions

import io.realm.kotlin.internal.toDuration
import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun RealmInstant.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(toDuration().inWholeMilliseconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}