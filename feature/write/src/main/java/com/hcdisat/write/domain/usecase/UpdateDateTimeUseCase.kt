package com.hcdisat.write.domain.usecase

import com.hcdisat.common.settings.Constants.CONVERSION_ZONE_ID
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

internal interface UpdateDateTimeUseCase {
    operator fun invoke(diaryTime: LocalDateTime, utcMillis: Long): Result<LocalDateTime>
}

internal class UpdateDateTimeUseCaseImpl @Inject constructor() : UpdateDateTimeUseCase {
    override fun invoke(diaryTime: LocalDateTime, utcMillis: Long): Result<LocalDateTime> =
        runCatching {
            Instant.ofEpochMilli(utcMillis)
                .atZone(ZoneId.of(CONVERSION_ZONE_ID))
                .toLocalDateTime()
                .withHour(diaryTime.hour)
                .withMinute(diaryTime.minute)
        }
}