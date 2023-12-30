package com.hcdisat.dairyapp.dataaccess.di

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
}