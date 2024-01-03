package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.core.settings.Constants.CONVERSION_ZONE_ID
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
                .atZone(ZoneId.of(CONVERSION_ZONE_ID))
                .toLocalDateTime()
                .withHour(diaryTime.hour)
                .withMinute(diaryTime.minute)
        }
}