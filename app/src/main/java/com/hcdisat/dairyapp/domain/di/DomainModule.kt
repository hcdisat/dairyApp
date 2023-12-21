package com.hcdisat.dairyapp.domain.di

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.abstraction.networking.CreateAccountService
import com.hcdisat.dairyapp.abstraction.networking.LogoutAccountService
import com.hcdisat.dairyapp.abstraction.networking.SessionService
import com.hcdisat.dairyapp.dataaccess.realm.services.CreateAccountServiceImpl
import com.hcdisat.dairyapp.dataaccess.realm.services.LogoutAccountServiceImpl
import com.hcdisat.dairyapp.dataaccess.realm.services.SessionServiceImpl
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