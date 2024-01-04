package com.hcdisat.dairyapp.domain.repository

import android.net.Uri
import com.hcdisat.abstraction.domain.model.ImageUploadResult
import com.hcdisat.abstraction.domain.model.ImageUploadRetry
import com.hcdisat.abstraction.domain.repository.ImageToDeleteRepository
import com.hcdisat.abstraction.domain.repository.ImageUploadRetryRepository
import com.hcdisat.dairyapp.dataaccess.firebase.DeleteRemoteImageService
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
    suspend fun removeImages(paths: List<String>): Result<Unit>
    suspend fun removeImage(remotePath: String): Result<Boolean>
    suspend fun removeAllImages(): Result<Unit>
}

class DomainImageRepositoryImpl @Inject constructor(
    private val imageUploaderService: ImageUploaderService,
    private val imageReaderService: ImageReaderService,
    private val retryRepository: ImageUploadRetryRepository,
    private val deleteRemoteImageService: DeleteRemoteImageService,
    private val deleteRepository: ImageToDeleteRepository
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

    override suspend fun removeImages(paths: List<String>) = coroutineScope {
        deleteRemoteImageService.deleteRemoteImages(
            remotePaths = paths,
            onEach = { launch(NonCancellable) { deleteRepository.insert(it) } },
            onEachDone = { launch(NonCancellable) { deleteRepository.deleteImage(it) } }
        )
    }

    override suspend fun removeImage(remotePath: String): Result<Boolean> =
        deleteRemoteImageService.deleteRemoteImage(remotePath).mapCatching {
            deleteRepository.deleteImage(it.remotePath)
            true
        }

    override suspend fun removeAllImages(): Result<Unit> = coroutineScope {
        deleteRemoteImageService.deleteAllImages {
            launch(NonCancellable) { deleteRepository.insert(it) }
        }
    }

    private fun UploadSession.toRetryUpload(): ImageUploadRetry = ImageUploadRetry(
        sessionUri = sessionUri.toString(),
        imageUri = imageData.toString(),
        remotePath = remotePath
    )
}