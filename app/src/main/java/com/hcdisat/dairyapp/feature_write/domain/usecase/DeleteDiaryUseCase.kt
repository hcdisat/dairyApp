package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import javax.inject.Inject

interface DeleteDiaryUseCase {
    suspend operator fun invoke(entryId: String): Result<Boolean>
}

class DeleteDiaryUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : DeleteDiaryUseCase {
    override suspend fun invoke(entryId: String) = mongoRepository.deleteDiary(entryId)
}