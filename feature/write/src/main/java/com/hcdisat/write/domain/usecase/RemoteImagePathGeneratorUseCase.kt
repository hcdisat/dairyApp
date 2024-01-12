package com.hcdisat.write.domain.usecase

import android.net.Uri
import com.hcdisat.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.common.UserNotAuthenticatedException
import com.hcdisat.ui.model.GalleryImage
import javax.inject.Inject

@JvmInline
internal value class ImageData(val uri: Uri)

@JvmInline
internal value class ImageExtension(val value: String)

internal interface RemoteImagePathGeneratorUseCase {
    operator fun invoke(images: List<Pair<ImageData, ImageExtension>>): List<GalleryImage>
}

internal class RemoteImagePathGeneratorUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : RemoteImagePathGeneratorUseCase {
    override fun invoke(images: List<Pair<ImageData, ImageExtension>>): List<GalleryImage> {
        val uid = authenticationRepository.getUId() ?: throw UserNotAuthenticatedException()
        return images.map { (data, ext) ->
            GalleryImage(
                image = data.uri,
                remoteImagePath = buildString {
                    append("images")
                    append("/")
                    append(uid)
                    append("/")
                    append(data.uri.lastPathSegment.orEmpty())
                    append("-")
                    append(System.currentTimeMillis())
                    append(".")
                    append(ext.value)
                }
            )
        }
    }
}