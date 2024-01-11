package com.hcdisat.home.di

import com.hcdisat.home.HomeRoute
import com.hcdisat.home.HomeRouteImpl
import com.hcdisat.home.domain.usecase.DeleteAllDiariesUseCase
import com.hcdisat.home.domain.usecase.DeleteAllDiariesUseCaseImpl
import com.hcdisat.home.domain.usecase.FilterDiariesUseCase
import com.hcdisat.home.domain.usecase.FilterDiariesUseCaseImpl
import com.hcdisat.home.domain.usecase.GetDiariesUseCase
import com.hcdisat.home.domain.usecase.GetDiariesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
internal interface HomeModule {
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

@Module
@InstallIn(SingletonComponent::class)
interface HomeScreenModule {
    @Binds
    @Singleton
    fun bindsAuthenticationRoute(impl: HomeRouteImpl): HomeRoute
}