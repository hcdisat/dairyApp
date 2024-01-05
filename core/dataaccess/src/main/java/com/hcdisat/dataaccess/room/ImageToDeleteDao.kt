package com.hcdisat.dataaccess.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hcdisat.dataaccess.room.entities.ImageToDelete

@Dao
interface ImageToDeleteDao {
    @Query("SELECT * FROM images_to_delete ORDER BY createdAt ASC")
    suspend fun getAll(): List<ImageToDelete>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageToDelete: ImageToDelete)

    @Query("DELETE from images_to_delete WHERE remotePath = :remotePath")
    suspend fun deleteImage(remotePath: String)
}