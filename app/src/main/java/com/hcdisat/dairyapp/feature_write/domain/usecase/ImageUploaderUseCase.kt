package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadResult
import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import javax.inject.Inject

interface ImageUploaderUseCase {
    suspend operator fun invoke(newImages: List<GalleryImage>): Result<ImageUploadResult>
}

class ImageUploaderUseCaseImpl @Inject constructor(
    private val imageRepository: DomainImageRepository
) : ImageUploaderUseCase {
    override suspend fun invoke(newImages: List<GalleryImage>): Result<ImageUploadResult> =
        runCatching {
            return imageRepository.uploadImages(
                newImages.map { it.remoteImagePath to it.image }
            )
        }
}