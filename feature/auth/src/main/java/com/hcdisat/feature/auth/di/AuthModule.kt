package com.hcdisat.feature.auth.di

import com.hcdisat.feature.auth.domain.SignInUseCase
import com.hcdisat.feature.auth.domain.SignInUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface AuthModule {
    @Binds
    @ViewModelScoped
    fun bindsSignInUseCase(impl: SignInUseCaseImpl): SignInUseCase
}