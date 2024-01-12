package com.hcdisat.write.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import javax.inject.Inject

internal interface DeleteDiaryUseCase {
    suspend operator fun invoke(entryId: String): Result<Boolean>
}

internal class DeleteDiaryUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : DeleteDiaryUseCase {
    override suspend fun invoke(entryId: String) = mongoRepository.deleteDiary(entryId)
}