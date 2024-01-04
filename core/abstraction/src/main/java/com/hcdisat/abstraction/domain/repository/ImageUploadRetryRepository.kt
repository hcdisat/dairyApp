package com.hcdisat.abstraction.domain.repository

import com.hcdisat.abstraction.domain.model.ImageUploadRetry

interface ImageUploadRetryRepository {
    suspend fun getAllImages(): List<ImageUploadRetry>
    suspend fun addImage(image: ImageUploadRetry)
    suspend fun removeImage(sessionUri: String)
}