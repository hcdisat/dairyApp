package com.hcdisat.dairyapp.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadRetry
import com.hcdisat.dairyapp.abstraction.domain.repository.ImageUploadRetryRepository
import com.hcdisat.dairyapp.dataaccess.room.ImageToUploadDao
import com.hcdisat.dairyapp.dataaccess.room.entities.ImageToUpload
import javax.inject.Inject

class ImageUploadRetryRepositoryImpl @Inject constructor(
    private val imageDao: ImageToUploadDao
) : ImageUploadRetryRepository {
    override suspend fun getAllImages(): List<ImageUploadRetry> =
        imageDao.getAllImages().map { it.toDomain() }

    override suspend fun addImage(image: ImageUploadRetry) {
        imageDao.addImage(image.toDataModel())
    }

    override suspend fun removeImage(sessionUri: String) {
        imageDao.removeImage(sessionUri)
    }

    private fun ImageToUpload.toDomain(): ImageUploadRetry =
        ImageUploadRetry(
            remotePath = remotePath,
            imageUri = imageUri,
            sessionUri = sessionUri
        )

    private fun ImageUploadRetry.toDataModel(): ImageToUpload =
        ImageToUpload(
            remotePath = remotePath,
            imageUri = imageUri,
            sessionUri = sessionUri
        )
}