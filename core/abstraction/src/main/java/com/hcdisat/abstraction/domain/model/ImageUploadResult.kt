package com.hcdisat.abstraction.domain.model

sealed interface ImageUploadResult {
    data object Success : ImageUploadResult
    data object Canceled : ImageUploadResult
    data class Error(val throwable: Throwable) : ImageUploadResult
}