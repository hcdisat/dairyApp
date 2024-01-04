package com.hcdisat.dairyapp.feature_home.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
import javax.inject.Inject

interface DeleteAllDiariesUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class DeleteAllDiariesUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository,
    private val imageRepository: DomainImageRepository,
) : DeleteAllDiariesUseCase {
    override suspend fun invoke(): Result<Unit> =
        mongoRepository.deleteAllDiaries().mapCatching {
            imageRepository.removeAllImages()
        }
}