package com.hcdisat.common

import android.net.Uri

class UserNotAuthenticatedException : RuntimeException("User is not authenticated")

data class RealmGenericException(val throwable: Throwable) : RuntimeException(throwable)

class InvalidImageUriException(uri: Uri) : RuntimeException("${uri.path}")

class OperationCanceledException(
    override val message: String = "Operation was Cancelled"
) : RuntimeException(message)

class ImageNotFoundException(imagePath: String) :
    RuntimeException("Image: $imagePath was not found")