package com.hcdisat.auth.di

import com.hcdisat.auth.AuthenticationRoute
import com.hcdisat.auth.AuthenticationRouteImpl
import com.hcdisat.auth.domain.SignInUseCase
import com.hcdisat.auth.domain.SignInUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface AuthModule {

    @Binds
    @ViewModelScoped
    fun bindsSignInUseCase(impl: SignInUseCaseImpl): SignInUseCase
}

@Module
@InstallIn(SingletonComponent::class)
interface ScreenModule {
    @Binds
    @Singleton
    fun bindsAuthenticationFeature(impl: AuthenticationRouteImpl): AuthenticationRoute
}