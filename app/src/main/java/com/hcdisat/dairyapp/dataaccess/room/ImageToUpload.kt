package com.hcdisat.dairyapp.dataaccess.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

private const val IMAGE_TO_UPLOAD_TABLE = "image_to_upload"

@Entity(tableName = IMAGE_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = false)
    val sessionUri: String,
    val remotePath: String,
    val imageUri: String,
    val createdAt: Long = Instant.now().toEpochMilli()
)
