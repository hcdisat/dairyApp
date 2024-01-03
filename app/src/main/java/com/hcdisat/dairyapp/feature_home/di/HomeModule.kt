package com.hcdisat.dairyapp.feature_home.di

import com.hcdisat.dairyapp.feature_home.domain.usecase.DeleteAllDiariesUseCase
import com.hcdisat.dairyapp.feature_home.domain.usecase.DeleteAllDiariesUseCaseImpl
import com.hcdisat.dairyapp.feature_home.domain.usecase.FilterDiariesUseCase
import com.hcdisat.dairyapp.feature_home.domain.usecase.FilterDiariesUseCaseImpl
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
    fun bindsDeleteAllDiariesUseCase(impl: DeleteAllDiariesUseCaseImpl): DeleteAllDiariesUseCase

    @Binds
    @ViewModelScoped
    fun bindsFilterDiariesUseCase(impl: FilterDiariesUseCaseImpl): FilterDiariesUseCase
}