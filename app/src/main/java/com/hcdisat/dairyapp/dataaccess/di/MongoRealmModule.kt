package com.hcdisat.dairyapp.dataaccess.di

import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseSignInService
import com.hcdisat.dairyapp.dataaccess.firebase.FirebaseSignInServiceImpl
import com.hcdisat.dairyapp.dataaccess.firebase.GoogleCredentialsProvider
import com.hcdisat.dairyapp.dataaccess.firebase.GoogleCredentialsProviderImpl
import com.hcdisat.dairyapp.dataaccess.firebase.ImageReaderService
import com.hcdisat.dairyapp.dataaccess.firebase.ImageReaderServiceImpl
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderService
import com.hcdisat.dairyapp.dataaccess.firebase.ImageUploaderServiceImpl
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabaseImpl
import com.hcdisat.dairyapp.dataaccess.realm.QueryProvider
import com.hcdisat.dairyapp.dataaccess.realm.QueryProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MongoRealmModule {
    @Binds
    @Singleton
    fun bindsQueryProvider(provider: QueryProviderImpl): QueryProvider

    @Binds
    @Singleton
    fun bindsMongoDatabase(databaseImpl: MongoDatabaseImpl): MongoDatabase

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
}