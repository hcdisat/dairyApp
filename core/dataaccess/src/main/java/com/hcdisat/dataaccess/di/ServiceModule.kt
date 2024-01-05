package com.hcdisat.dataaccess.di

import com.hcdisat.dataaccess.ConfigureDataServices
import com.hcdisat.dataaccess.ConfigureDataServicesImpl
import com.hcdisat.dataaccess.firebase.DeleteImageServiceImpl
import com.hcdisat.dataaccess.firebase.DeleteRemoteImageService
import com.hcdisat.dataaccess.firebase.FirebaseSignInService
import com.hcdisat.dataaccess.firebase.FirebaseSignInServiceImpl
import com.hcdisat.dataaccess.firebase.GoogleCredentialsProvider
import com.hcdisat.dataaccess.firebase.GoogleCredentialsProviderImpl
import com.hcdisat.dataaccess.firebase.ImageReaderService
import com.hcdisat.dataaccess.firebase.ImageReaderServiceImpl
import com.hcdisat.dataaccess.firebase.ImageUploaderService
import com.hcdisat.dataaccess.firebase.ImageUploaderServiceImpl
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
    fun bindsDeleterImageService(impl: DeleteImageServiceImpl): DeleteRemoteImageService

    @Binds
    @Singleton
    fun bindsConfigureServices(impl: ConfigureDataServicesImpl): ConfigureDataServices
}