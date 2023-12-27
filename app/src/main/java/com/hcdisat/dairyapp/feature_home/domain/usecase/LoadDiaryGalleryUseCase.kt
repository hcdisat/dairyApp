package com.hcdisat.dairyapp.feature_home.domain.usecase

import android.net.Uri
import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
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