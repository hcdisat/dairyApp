package com.hcdisat.dairyapp.dataaccess.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

private const val IMAGE_TO_DELETE_TABLE = "images_to_delete"

@Entity(tableName = IMAGE_TO_DELETE_TABLE)
data class ImageToDelete(
    @PrimaryKey(autoGenerate = false)
    val remotePath: String = "",
    val createdAt: Long = Instant.now().toEpochMilli()
)
