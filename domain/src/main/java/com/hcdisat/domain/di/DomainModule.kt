package com.hcdisat.domain.di

import com.hcdisat.abstraction.domain.repository.AuthenticationRepository
import com.hcdisat.abstraction.domain.repository.ImageToDeleteRepository
import com.hcdisat.abstraction.domain.repository.ImageUploadRetryRepository
import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.abstraction.networking.CreateAccountService
import com.hcdisat.abstraction.networking.LogoutAccountService
import com.hcdisat.abstraction.networking.SessionService
import com.hcdisat.dataaccess.realm.services.CreateAccountServiceImpl
import com.hcdisat.dataaccess.realm.services.LogoutAccountServiceImpl
import com.hcdisat.dataaccess.realm.services.SessionServiceImpl
import com.hcdisat.domain.repository.AuthenticationRepositoryImpl
import com.hcdisat.domain.repository.DomainImageRepository
import com.hcdisat.domain.repository.DomainImageRepositoryImpl
import com.hcdisat.domain.repository.ImageToDeleteRepositoryImpl
import com.hcdisat.domain.repository.ImageUploadRetryRepositoryImpl
import com.hcdisat.domain.repository.MongoRepositoryImpl
import com.hcdisat.domain.usecases.LoadDiaryGalleryUseCase
import com.hcdisat.domain.usecases.LoadDiaryGalleryUseCaseImpl
import com.hcdisat.domain.usecases.ResumeImagesRemovalUseCase
import com.hcdisat.domain.usecases.ResumeImagesRemovalUseCaseImpl
import com.hcdisat.domain.usecases.RetryImageUploadUseCase
import com.hcdisat.domain.usecases.RetryImageUploadUseCaseImpl
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

    @Binds
    @ViewModelScoped
    fun bindsAuthenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    @ViewModelScoped
    fun bindsDomainImageRepository(impl: DomainImageRepositoryImpl): DomainImageRepository

    @Binds
    @ViewModelScoped
    fun bindsImageUploadRetryRepository(impl: ImageUploadRetryRepositoryImpl): ImageUploadRetryRepository

    @Binds
    @ViewModelScoped
    fun bindsRetryImageUploadUseCase(impl: RetryImageUploadUseCaseImpl): RetryImageUploadUseCase

    @Binds
    @ViewModelScoped
    fun bindsImageToDeleteRepository(impl: ImageToDeleteRepositoryImpl): ImageToDeleteRepository

    @Binds
    @ViewModelScoped
    fun bindsResumeImagesRemovalUseCase(impl: ResumeImagesRemovalUseCaseImpl): ResumeImagesRemovalUseCase

    @Binds
    @ViewModelScoped
    fun bindsLoadDiaryGalleryUseCase(impl: LoadDiaryGalleryUseCaseImpl): LoadDiaryGalleryUseCase
}