package com.hcdisat.dairyapp.abstraction.domain.model

data class ImageUploadRetry(
    val id: Int = 0,
    val remotePath: String = "",
    val imageUri: String = "",
    val sessionUri: String = ""
)