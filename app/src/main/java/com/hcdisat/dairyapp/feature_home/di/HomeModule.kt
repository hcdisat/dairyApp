package com.hcdisat.dairyapp.feature_home.di

import com.hcdisat.dairyapp.domain.usecases.LoadDiaryGalleryUseCase
import com.hcdisat.dairyapp.domain.usecases.LoadDiaryGalleryUseCaseImpl
import com.hcdisat.dairyapp.feature_home.domain.usecase.GetDiariesUseCase
import com.hcdisat.dairyapp.feature_home.domain.usecase.GetDiariesUseCaseImpl
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
    fun bindGetDiariesUseCase(impl: GetDiariesUseCaseImpl): GetDiariesUseCase

    @Binds
    @ViewModelScoped
    fun bindsLoadDiaryGalleryUseCase(impl: LoadDiaryGalleryUseCaseImpl): LoadDiaryGalleryUseCase
}