package com.hcdisat.dairyapp.feature_home.domain.usecase

import com.hcdisat.common.settings.Constants
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

interface FilterDiariesUseCase {
    operator fun invoke(dateTime: LocalDateTime, utcDateTimeInMillis: Long): Boolean
}

class FilterDiariesUseCaseImpl @Inject constructor() : FilterDiariesUseCase {
    override fun invoke(dateTime: LocalDateTime, utcDateTimeInMillis: Long): Boolean {
        val utcDateTime = Instant.ofEpochMilli(utcDateTimeInMillis)
            .atZone(ZoneId.of(Constants.CONVERSION_ZONE_ID))
            .toLocalDate()

        return utcDateTime == dateTime.toLocalDate()
    }
}