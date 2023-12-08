package com.hcdisat.dairyapp.core.extensions

import io.realm.kotlin.internal.toDuration
import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun RealmInstant.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(toDuration().inWholeMilliseconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}