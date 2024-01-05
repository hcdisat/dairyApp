package com.hcdisat.dataaccess.di

import android.content.Context
import com.hcdisat.dataaccess.room.ImageToDeleteDao
import com.hcdisat.dataaccess.room.ImageToUploadDao
import com.hcdisat.dataaccess.room.ImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    @Singleton
    fun providesImagesDatabase(@ApplicationContext context: Context): ImagesDatabase =
        ImagesDatabase.create(context)

    @Provides
    @Singleton
    fun providesImageDao(database: ImagesDatabase): ImageToUploadDao = database.imageToUploadDao()

    @Provides
    @Singleton
    fun providesImageToDeleteDao(database: ImagesDatabase): ImageToDeleteDao =
        database.imageToDeleteDao()
}