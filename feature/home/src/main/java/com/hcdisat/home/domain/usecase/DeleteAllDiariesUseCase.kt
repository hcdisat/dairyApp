package com.hcdisat.home.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.domain.repository.DomainImageRepository
import javax.inject.Inject

internal interface DeleteAllDiariesUseCase {
    suspend operator fun invoke(): Result<Unit>
}

internal class DeleteAllDiariesUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository,
    private val imageRepository: DomainImageRepository,
) : DeleteAllDiariesUseCase {
    override suspend fun invoke(): Result<Unit> =
        mongoRepository.deleteAllDiaries().mapCatching {
            imageRepository.removeAllImages()
        }
}