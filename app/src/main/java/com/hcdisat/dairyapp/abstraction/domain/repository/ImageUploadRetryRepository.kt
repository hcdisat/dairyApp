package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadRetry

interface ImageUploadRetryRepository {
    suspend fun getAllImages(): List<ImageUploadRetry>
    suspend fun addImage(image: ImageUploadRetry)
    suspend fun removeImage(sessionUri: String)
}