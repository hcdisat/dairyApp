package com.hcdisat.dataaccess.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

private const val IMAGES_TO_UPLOAD_TABLE = "images_to_upload"

@Entity(tableName = IMAGES_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = false)
    val sessionUri: String,
    val remotePath: String,
    val imageUri: String,
    val createdAt: Long = Instant.now().toEpochMilli()
)