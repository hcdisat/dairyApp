package com.hcdisat.domain.usecases

import android.util.Log
import androidx.core.net.toUri
import com.hcdisat.abstraction.domain.model.ImageUploadRetry
import com.hcdisat.abstraction.domain.repository.ImageUploadRetryRepository
import com.hcdisat.dataaccess.firebase.UploadSession
import com.hcdisat.domain.repository.DomainImageRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

interface RetryImageUploadUseCase {
    suspend operator fun invoke()
}

private const val TAG = "RetryImageUploadUseCaseImpl"

class RetryImageUploadUseCaseImpl @Inject constructor(
    private val imageRepository: DomainImageRepository,
    private val retryRepository: ImageUploadRetryRepository
) : RetryImageUploadUseCase {
    override suspend fun invoke() {
        val sessionsToResume = retryRepository.getAllImages().map { it.uploadSession() }
        if (sessionsToResume.isEmpty()) return

        sessionsToResume.forEach { session ->
            withContext(coroutineContext) { imageRepository.retryUpLoadImage(session) }
                .onSuccess { retryRepository.removeImage(session.sessionUri.toString()) }
                .onFailure {
                    Log.e(TAG, "invoke: ${it.message}", it)
                }
        }
    }

    private fun ImageUploadRetry.uploadSession(): UploadSession = UploadSession(
        sessionUri = sessionUri.toUri(),
        imageData = imageUri.toUri(),
        remotePath = remotePath
    )
}