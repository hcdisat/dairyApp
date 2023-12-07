package com.hcdisat.dairyapp.feature_home.di

import com.hcdisat.dairyapp.feature_home.domain.repository.DiaryRepository
import com.hcdisat.dairyapp.feature_home.domain.repository.DiaryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface HomeModule {
    @Binds
    @ViewModelScoped
    fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository
}