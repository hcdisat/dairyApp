package com.hcdisat.dairyapp.domain.repository

import android.net.Uri
import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadResult
import com.hcdisat.dairyapp.abstraction.domain.model.ImageUploadRetry
import com.hcdisat.dairyapp.abstraction.domain.repository.ImageUploadRetryRepository
import com.hcdisat.dairyapp.dataaccess.firebase.ImageReaderService
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderService
import com.hcdisat.dairyapp.dataaccess.firebase.UploadSession
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DomainImageRepository {
    suspend fun uploadImagesWithResult(newImages: List<Pair<String, Uri>>): Result<ImageUploadResult>
    suspend fun retryUpLoadImage(session: UploadSession): Result<ImageUploadResult>
    suspend fun uploadImages(newImages: List<Pair<String, Uri>>)
    suspend fun downloadImages(paths: List<String>): Result<List<Uri>>
}

class DomainImageRepositoryImpl @Inject constructor(
    private val imageUploaderService: ImageUploaderService,
    private val imageReaderService: ImageReaderService,
    private val retryRepository: ImageUploadRetryRepository
) : DomainImageRepository {
    override suspend fun uploadImagesWithResult(
        newImages: List<Pair<String, Uri>>
    ): Result<ImageUploadResult> = runCatching {
        imageUploaderService.uploadImagesWithResult(newImages)
    }

    override suspend fun retryUpLoadImage(
        session: UploadSession
    ): Result<ImageUploadResult> = runCatching {
        imageUploaderService.resumeImageUpload(session)
    }

    override suspend fun uploadImages(newImages: List<Pair<String, Uri>>) = coroutineScope {
        imageUploaderService.uploadImages(newImages) {
            launch(NonCancellable) {
                retryRepository.addImage(toRetryUpload())
                if (bytesTransferred == totalByteCount) {
                    retryRepository.removeImage(sessionUri.toString())
                }
            }
        }
    }

    override suspend fun downloadImages(paths: List<String>): Result<List<Uri>> =
        imageReaderService.getImagesFromPaths(paths)

    private fun UploadSession.toRetryUpload(): ImageUploadRetry = ImageUploadRetry(
        sessionUri = sessionUri.toString(),
        imageUri = imageData.toString(),
        remotePath = remotePath
    )
}