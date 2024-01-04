package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.ui.extensions.toDomain
import com.hcdisat.ui.extensions.toPresentationDiary
import com.hcdisat.ui.model.PresentationDiary
import javax.inject.Inject

interface SaveDiaryUseCase {
    suspend operator fun invoke(diary: PresentationDiary): Result<PresentationDiary>
}

class SaveDiaryUseCaseImpl @Inject constructor(
    private val diaryRepository: MongoRepository
) : SaveDiaryUseCase {
    override suspend fun invoke(diary: PresentationDiary): Result<PresentationDiary> =
        diaryRepository.saveDiary(diary.toDomain()).mapCatching { it.toPresentationDiary() }
}