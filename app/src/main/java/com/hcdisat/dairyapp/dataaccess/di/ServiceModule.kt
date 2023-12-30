package com.hcdisat.dairyapp.dataaccess.di

import com.hcdisat.dairyapp.dataaccess.firebase.DeleteImageService
import com.hcdisat.dairyapp.dataaccess.firebase.DeleteImageServiceImpl
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseSignInService
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseSignInServiceImpl
import com.hcdisat.dairyapp.dataaccess.firebase.GoogleCredentialsProvider
import com.hcdisat.dairyapp.dataaccess.firebase.GoogleCredentialsProviderImpl
import com.hcdisat.dairyapp.dataaccess.firebase.ImageReaderService
import com.hcdisat.dairyapp.dataaccess.firebase.ImageReaderServiceImpl
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderService
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {
    @Binds
    @Singleton
    fun bindsGoogleCredentialsProvider(impl: GoogleCredentialsProviderImpl): GoogleCredentialsProvider

    @Binds
    @Singleton
    fun bindsFirebaseSignInService(impl: FirebaseSignInServiceImpl): FirebaseSignInService

    @Binds
    @Singleton
    fun bindsImageRepository(impl: ImageUploaderServiceImpl): ImageUploaderService

    @Binds
    @Singleton
    fun bindsImageReaderService(impl: ImageReaderServiceImpl): ImageReaderService

    @Binds
    @Singleton
    fun bindsDeleterImageService(impl: DeleteImageServiceImpl): DeleteImageService
}