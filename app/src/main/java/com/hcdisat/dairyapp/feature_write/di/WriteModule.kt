package com.hcdisat.dairyapp.feature_write.di

import android.content.Context
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.DeleteDiaryUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.ErrorHandlerUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ErrorHandlerUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.GetSingleDiaryUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageUploaderUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.ImageUploaderUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.SaveDiaryUseCaseImpl
import com.hcdisat.dairyapp.feature_write.domain.usecase.UpdateDateTimeUseCase
import com.hcdisat.dairyapp.feature_write.domain.usecase.UpdateDateTimeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Binds
    @ViewModelScoped
    fun bindsDeleteDiaryUseCase(impl: DeleteDiaryUseCaseImpl): DeleteDiaryUseCase

    @Binds
    @ViewModelScoped
    fun bindsErrorHandlerUseCase(impl: ErrorHandlerUseCaseImpl): ErrorHandlerUseCase

    @Binds
    @ViewModelScoped
    fun bindsImageUploaderUseCase(impl: ImageUploaderUseCaseImpl): ImageUploaderUseCase
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelUtilsModule {
    @Provides
    @ViewModelScoped
    fun providesResources(@ApplicationContext context: Context) = context.resources
}