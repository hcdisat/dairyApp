package com.hcdisat.dataaccess.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hcdisat.dataaccess.room.entities.ImageToDelete
import com.hcdisat.dataaccess.room.entities.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 2,
    exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImageToDeleteDao

    companion object {
        private const val NAME = "images_db"

        fun create(context: Context): ImagesDatabase {
            return Room.databaseBuilder(context, ImagesDatabase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}