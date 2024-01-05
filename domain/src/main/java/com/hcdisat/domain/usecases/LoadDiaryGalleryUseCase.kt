package com.hcdisat.domain.usecases

import android.net.Uri
import com.hcdisat.domain.repository.DomainImageRepository
import javax.inject.Inject

interface LoadDiaryGalleryUseCase {
    suspend operator fun invoke(imagesPaths: List<String>): Result<List<Uri>>
}

class LoadDiaryGalleryUseCaseImpl @Inject constructor(
    private val imageRepository: DomainImageRepository
) : LoadDiaryGalleryUseCase {
    override suspend fun invoke(imagesPaths: List<String>): Result<List<Uri>> =
        imageRepository.downloadImages(imagesPaths)
}