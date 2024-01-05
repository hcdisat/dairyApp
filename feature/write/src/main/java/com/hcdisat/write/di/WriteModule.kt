package com.hcdisat.write.di

import android.content.Context
import com.hcdisat.write.domain.usecase.DeleteDiaryUseCase
import com.hcdisat.write.domain.usecase.DeleteDiaryUseCaseImpl
import com.hcdisat.write.domain.usecase.DeleteImageUseCase
import com.hcdisat.write.domain.usecase.DeleteImageUseCaseImpl
import com.hcdisat.write.domain.usecase.ErrorHandlerUseCase
import com.hcdisat.write.domain.usecase.ErrorHandlerUseCaseImpl
import com.hcdisat.write.domain.usecase.GetSingleDiaryUseCase
import com.hcdisat.write.domain.usecase.GetSingleDiaryUseCaseImpl
import com.hcdisat.write.domain.usecase.ImageUploaderUseCase
import com.hcdisat.write.domain.usecase.ImageUploaderUseCaseImpl
import com.hcdisat.write.domain.usecase.RemoteImagePathGeneratorUseCase
import com.hcdisat.write.domain.usecase.RemoteImagePathGeneratorUseCaseImpl
import com.hcdisat.write.domain.usecase.SaveDiaryUseCase
import com.hcdisat.write.domain.usecase.SaveDiaryUseCaseImpl
import com.hcdisat.write.domain.usecase.UpdateDateTimeUseCase
import com.hcdisat.write.domain.usecase.UpdateDateTimeUseCaseImpl
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

    @Binds
    @ViewModelScoped
    fun bindsRemoteImagePathGeneratorUseCase(
        impl: RemoteImagePathGeneratorUseCaseImpl
    ): RemoteImagePathGeneratorUseCase

    @Binds
    @ViewModelScoped
    fun bindsDeleteImageUseCase(impl: DeleteImageUseCaseImpl): DeleteImageUseCase
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelUtilsModule {
    @Provides
    @ViewModelScoped
    fun providesResources(@ApplicationContext context: Context) = context.resources
}