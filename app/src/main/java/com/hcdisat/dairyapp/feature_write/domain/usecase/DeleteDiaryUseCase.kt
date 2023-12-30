package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
import javax.inject.Inject

interface DeleteDiaryUseCase {
    suspend operator fun invoke(entryId: String): Result<Boolean>
}

class DeleteDiaryUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository,
    private val imageRepository: DomainImageRepository
) : DeleteDiaryUseCase {
    override suspend fun invoke(entryId: String) = mongoRepository.deleteDiary(entryId)
}