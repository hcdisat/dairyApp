package com.hcdisat.dairyapp.dataaccess.room

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val IMAGE_TO_UPLOAD_TABLE = "image_to_upload"

@Entity(tableName = IMAGE_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remotePath: String,
    val imageUri: String,
    val sessionUri: String
)
