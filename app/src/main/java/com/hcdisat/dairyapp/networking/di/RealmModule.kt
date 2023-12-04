package com.hcdisat.dairyapp.networking.di

import com.hcdisat.dairyapp.settings.Constants.APP_ID
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
}