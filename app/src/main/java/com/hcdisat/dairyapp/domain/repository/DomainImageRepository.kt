package com.hcdisat.dairyapp.domain.repository

import android.net.Uri
import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadResult
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderService
import javax.inject.Inject

interface DomainImageRepository {
    suspend fun uploadImages(newImages: List<Pair<String, Uri>>): Result<ImageUploadResult>
}

class DomainImageRepositoryImpl @Inject constructor(
    private val imageUploaderService: ImageUploaderService
) : DomainImageRepository {
    override suspend fun uploadImages(
        newImages: List<Pair<String, Uri>>
    ): Result<ImageUploadResult> = runCatching {
        imageUploaderService.uploadImages(newImages)
    }
}