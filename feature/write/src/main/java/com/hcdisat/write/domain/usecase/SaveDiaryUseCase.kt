package com.hcdisat.write.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.ui.extensions.toDomain
import com.hcdisat.ui.extensions.toPresentationDiary
import com.hcdisat.ui.model.PresentationDiary
import javax.inject.Inject

internal interface SaveDiaryUseCase {
    suspend operator fun invoke(diary: PresentationDiary): Result<PresentationDiary>
}

internal class SaveDiaryUseCaseImpl @Inject constructor(
    private val diaryRepository: MongoRepository
) : SaveDiaryUseCase {
    override suspend fun invoke(diary: PresentationDiary): Result<PresentationDiary> =
        diaryRepository.saveDiary(diary.toDomain()).mapCatching { it.toPresentationDiary() }
}