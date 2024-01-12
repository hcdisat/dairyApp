package com.hcdisat.write.domain.usecase

import com.hcdisat.domain.repository.DomainImageRepository
import javax.inject.Inject

internal interface DeleteImageUseCase {
    suspend operator fun invoke(imageSet: Set<String>): Result<Unit>
}

internal class DeleteImageUseCaseImpl @Inject constructor(
    private val retryRepository: DomainImageRepository
) : DeleteImageUseCase {
    override suspend fun invoke(imageSet: Set<String>) =
        retryRepository.removeImages(imageSet.toList())
}