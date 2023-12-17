package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.toDomain
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDiary
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