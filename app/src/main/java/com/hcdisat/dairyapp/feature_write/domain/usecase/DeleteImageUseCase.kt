package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import javax.inject.Inject

interface DeleteImageUseCase {
    suspend operator fun invoke(imageSet: Set<GalleryImage>): Result<Unit>
}

class DeleteImageUseCaseImpl @Inject constructor(
    private val repository: DomainImageRepository
) : DeleteImageUseCase {
    override suspend fun invoke(imageSet: Set<GalleryImage>) = runCatching {
        repository.removeImages(imageSet.map { it.remoteImagePath })
    }
}