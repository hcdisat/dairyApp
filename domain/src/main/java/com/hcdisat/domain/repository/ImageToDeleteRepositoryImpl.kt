package com.hcdisat.domain.repository

import com.hcdisat.abstraction.domain.model.DomainImageToDelete
import com.hcdisat.abstraction.domain.repository.ImageToDeleteRepository
import com.hcdisat.dataaccess.room.ImageToDeleteDao
import com.hcdisat.dataaccess.room.entities.ImageToDelete
import javax.inject.Inject

class ImageToDeleteRepositoryImpl @Inject constructor(
    private val imageToDeleteDao: ImageToDeleteDao
) : ImageToDeleteRepository {
    override suspend fun getAll(): Result<List<DomainImageToDelete>> = runCatching {
        imageToDeleteDao.getAll().map { DomainImageToDelete(remotePath = it.remotePath) }
    }

    override suspend fun insert(imageToDelete: DomainImageToDelete) =
        insert(imageToDelete.remotePath)

    override suspend fun insert(remotePath: String) = runCatching {
        imageToDeleteDao.insert(ImageToDelete(remotePath = remotePath))
    }

    override suspend fun deleteImage(remotePath: String) = runCatching {
        imageToDeleteDao.deleteImage(remotePath)
    }
}