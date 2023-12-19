package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

interface UpdateDateTimeUseCase {
    operator fun invoke(diary: PresentationDiary, utcMillis: Long): Result<PresentationDiary>
}

class UpdateDateTimeUseCaseImpl @Inject constructor() : UpdateDateTimeUseCase {
    override fun invoke(diary: PresentationDiary, utcMillis: Long): Result<PresentationDiary> =
        runCatching {
            val newDate = Instant
                .ofEpochMilli(utcMillis)
                .atZone(ZoneId.of(ZONE_ID))
                .toLocalDateTime()
                .withHour(diary.dateTime.hour)
                .withMinute(diary.dateTime.minute)

            diary.copy(dateTime = newDate)
        }

    companion object {
        private const val ZONE_ID = "UTC"
    }
}