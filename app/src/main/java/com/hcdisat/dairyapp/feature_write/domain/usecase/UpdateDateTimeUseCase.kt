package com.hcdisat.dairyapp.feature_write.domain.usecase

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

interface UpdateDateTimeUseCase {
    operator fun invoke(diaryTime: LocalDateTime, utcMillis: Long): Result<LocalDateTime>
}

class UpdateDateTimeUseCaseImpl @Inject constructor() : UpdateDateTimeUseCase {
    override fun invoke(diaryTime: LocalDateTime, utcMillis: Long): Result<LocalDateTime> =
        runCatching {
            Instant.ofEpochMilli(utcMillis)
                .atZone(ZoneId.of(ZONE_ID))
                .toLocalDateTime()
                .withHour(diaryTime.hour)
                .withMinute(diaryTime.minute)
        }

    companion object {
        private const val ZONE_ID = "UTC"
    }
}