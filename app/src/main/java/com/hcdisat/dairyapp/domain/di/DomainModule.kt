package com.hcdisat.dairyapp.domain.di

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.domain.repository.MongoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {
    @Binds
    @ViewModelScoped
    fun bindsMongoRepository(impl: MongoRepositoryImpl): MongoRepository
}