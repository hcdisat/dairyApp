package com.hcdisat.dairyapp.dataaccess.di

import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabaseImpl
import com.hcdisat.dairyapp.dataaccess.realm.QueryProvider
import com.hcdisat.dairyapp.dataaccess.realm.QueryProviderImpl
import com.hcdisat.dairyapp.dataaccess.realm.repository.MongoDatabase
import com.hcdisat.dairyapp.settings.Constants.APP_ID
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {
    @Provides
    @Singleton
    fun providesRealApp(): App = App.create(APP_ID)

    @Module
    @InstallIn(SingletonComponent::class)
    interface SubModule {
        @Binds
        @Singleton
        fun bindsQueryProvider(provider: QueryProviderImpl): QueryProvider

        @Binds
        @Singleton
        fun bindsMongoDatabase(databaseImpl: MongoDatabaseImpl): MongoDatabase
    }
}