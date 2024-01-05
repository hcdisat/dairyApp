@file:Suppress("unused")

package com.hcdisat.dataaccess.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hcdisat.dataaccess.room.entities.ImageToUpload

@Dao
interface ImageToUploadDao {
    @Query("SELECT * FROM images_to_upload ORDER BY createdAt ASC")
    suspend fun getAllImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(image: ImageToUpload)

    @Query("DELETE FROM images_to_upload WHERE sessionUri = :sessionUri")
    suspend fun removeImage(sessionUri: String)
}