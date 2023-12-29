package com.hcdisat.dairyapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DairyApp : Application() {

    @Inject
    lateinit var mongoDatabase: MongoDatabase
    override fun onCreate() {
        super.onCreate()
        mongoDatabase.configureRealm()
        FirebaseApp.initializeApp(this)
    }
}