package com.hcdisat.dataaccess

import android.content.Context
import com.google.firebase.FirebaseApp
import com.hcdisat.dataaccess.realm.MongoDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ConfigureDataServices {
    fun configure()
}

class ConfigureDataServicesImpl @Inject constructor(
    private val mongoDatabase: MongoDatabase,
    @ApplicationContext private val context: Context
) : ConfigureDataServices {
    override fun configure() {
        FirebaseApp.initializeApp(context)
        mongoDatabase.configureRealm()
    }
}