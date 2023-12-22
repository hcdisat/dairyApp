package com.hcdisat.dairyapp.feature_write.domain.usecase

import android.net.Uri
import com.hcdisat.dairyapp.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.dairyapp.core.UserNotAuthenticatedException
import com.hcdisat.dairyapp.presentation.components.model.GalleryImage
import javax.inject.Inject

interface ImageUploaderUseCase {
    operator fun invoke(newImages: List<Pair<Uri, String>>): Result<List<GalleryImage>>
}

class ImageUploaderUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ImageUploaderUseCase {
    override fun invoke(
        newImages: List<Pair<Uri, String>>
    ): Result<List<GalleryImage>> = runCatching {
        getImagesRemotePath(newImages)
    }

    private fun getImagesRemotePath(images: List<Pair<Uri, String>>): List<GalleryImage> {
        val uid = authenticationRepository.user?.uid ?: throw UserNotAuthenticatedException()
        return images.map { (uri, ext) ->
            GalleryImage(
                image = uri,
                remoteImagePath = buildString {
                    append("images")
                    append("/")
                    append(uid)
                    append("/")
                    append(uri.lastPathSegment.orEmpty())
                    append("-")
                    append(System.currentTimeMillis())
                    append(".")
                    append(ext)
                }
            )
        }
    }
}