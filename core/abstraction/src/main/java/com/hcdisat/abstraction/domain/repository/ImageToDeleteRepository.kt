package com.hcdisat.abstraction.domain.repository

import com.hcdisat.abstraction.domain.model.DomainImageToDelete

interface ImageToDeleteRepository {
    suspend fun getAll(): Result<List<DomainImageToDelete>>
    suspend fun insert(imageToDelete: DomainImageToDelete): Result<Unit>
    suspend fun insert(remotePath: String): Result<Unit>
    suspend fun deleteImage(remotePath: String): Result<Unit>
}