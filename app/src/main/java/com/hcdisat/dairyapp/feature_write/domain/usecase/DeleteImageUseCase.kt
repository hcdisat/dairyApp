package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.domain.repository.DomainImageRepository
import javax.inject.Inject

interface DeleteImageUseCase {
    suspend operator fun invoke(imageSet: Set<String>): Result<Unit>
}

class DeleteImageUseCaseImpl @Inject constructor(
    private val retryRepository: DomainImageRepository
) : DeleteImageUseCase {
    override suspend fun invoke(imageSet: Set<String>) =
        retryRepository.removeImages(imageSet.toList())
}