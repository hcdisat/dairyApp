package com.hcdisat.dairyapp

import android.app.Application
import com.hcdisat.abstraction.networking.ConfigureServices
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DairyApp : Application() {

    @Inject
    lateinit var configurator: ConfigureServices
    override fun onCreate() {
        super.onCreate()
        configurator.initializeServices()
    }
}