package com.hcdisat.dairyapp.networking.di

import com.hcdisat.dairyapp.abstraction.networking.CreateAccountService
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.abstraction.networking.SessionService
import com.hcdisat.dairyapp.networking.atlas.CreateAccountServiceImpl
import com.hcdisat.dairyapp.networking.atlas.LogoutAccountServiceImpl
import com.hcdisat.dairyapp.networking.atlas.SessionServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface NetworkModule {
    @Binds
    @ViewModelScoped
    fun bindsCreateAccountService(impl: CreateAccountServiceImpl): CreateAccountService

    @Binds
    @ViewModelScoped
    fun bindsLogoutAccountService(impl: LogoutAccountServiceImpl): LogoutAccountService

    @Binds
    @ViewModelScoped
    fun bindsSessionService(impl: SessionServiceImpl): SessionService
}