package com.hcdisat.dairyapp.dataaccess.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        private const val NAME = "images_db"

        fun create(context: Context): ImagesDatabase {
            return Room.databaseBuilder(context, ImagesDatabase::class.java, NAME).build()
        }
    }
}