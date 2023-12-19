package com.hcdisat.dairyapp.feature_write.di

import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.UpdateDateTimeUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.UpdateDateTimeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface WriteModule {
    @Binds
    @ViewModelScoped
    fun bindsGetSingleDiaryUseCase(impl: GetSingleDiaryUseCaseImpl): GetSingleDiaryUseCase

    @Binds
    @ViewModelScoped
    fun bindsSaveDiaryUseCase(impl: SaveDiaryUseCaseImpl): SaveDiaryUseCase

    @Binds
    @ViewModelScoped
    fun bindsUpdateDateTimeUseCase(impl: UpdateDateTimeUseCaseImpl): UpdateDateTimeUseCase
}