package com.hcdisat.write.domain.usecase

import com.hcdisat.domain.repository.DomainImageRepository
import com.hcdisat.ui.model.GalleryImage
import javax.inject.Inject

interface ImageUploaderUseCase {
    suspend operator fun invoke(newImages: List<GalleryImage>)
}

class ImageUploaderUseCaseImpl @Inject constructor(
    private val imageRepository: DomainImageRepository
) : ImageUploaderUseCase {
    override suspend fun invoke(newImages: List<GalleryImage>) {
        imageRepository.uploadImages(newImages.map { it.remoteImagePath to it.image })
    }
}