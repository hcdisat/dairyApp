package com.hcdisat.domain.di

import com.hcdisat.abstraction.networking.ConfigureServices
import com.hcdisat.domain.ConfigureServicesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ConfigModule {
    @Binds
    @Singleton
    fun bindsConfigureServices(impl: ConfigureServicesImpl): ConfigureServices
}