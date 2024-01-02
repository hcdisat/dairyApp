package com.hcdisat.dairyapp.core.di

import android.content.Context
import android.net.ConnectivityManager
import com.hcdisat.dairyapp.core.conectivity.ConnectivityObserverService
import com.hcdisat.dairyapp.core.conectivity.ConnectivityObserverServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun providesConnectivityObserverService(
        @ApplicationContext context: Context
    ): ConnectivityObserverService {
        return ConnectivityObserverServiceImpl(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }
}