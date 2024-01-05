package com.hcdisat.dataaccess.di

import com.hcdisat.dataaccess.realm.MongoDatabase
import com.hcdisat.dataaccess.realm.MongoDatabaseImpl
import com.hcdisat.dataaccess.realm.QueryProvider
import com.hcdisat.dataaccess.realm.QueryProviderImpl
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