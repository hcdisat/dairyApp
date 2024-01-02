package com.hcdisat.dairyapp.domain.usecases

import android.util.Log
import com.hcdisat.dairyapp.abstraction.domain.repository.ImageToDeleteRepository
import com.hcdisat.dairyapp.domain.repository.DomainImageRepository
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
        throw it
        Log.e(
            "ResumeImagesRemovalUseCaseImpl",
            "images were not deleted: ${it.message}",
            it
        )
    }
}