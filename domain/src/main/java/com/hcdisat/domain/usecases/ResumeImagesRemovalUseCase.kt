package com.hcdisat.domain.usecases

import android.util.Log
import com.hcdisat.abstraction.domain.repository.ImageToDeleteRepository
import com.hcdisat.domain.repository.DomainImageRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

interface ResumeImagesRemovalUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class ResumeImagesRemovalUseCaseImpl @Inject constructor(
    private val deleteImageRepository: ImageToDeleteRepository,
    private val imageRepository: DomainImageRepository
) : ResumeImagesRemovalUseCase {
    override suspend fun invoke() = deleteImageRepository.getAll().mapCatching { targets ->
        targets.forEach { target ->
            withContext(coroutineContext) {
                imageRepository.removeImage(target.remotePath).mapCatching {
                    if (it) deleteImageRepository.deleteImage(target.remotePath)
                }
            }
        }
    }.onFailure {
        Log.e(
            "ResumeImagesRemovalUseCaseImpl",
            "images were not deleted: ${it.message}",
            it
        )
    }
}