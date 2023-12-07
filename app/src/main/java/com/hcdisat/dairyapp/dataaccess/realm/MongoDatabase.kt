package com.hcdisat.dairyapp.dataaccess.realm

import com.hcdisat.dairyapp.dataaccess.realm.model.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoDatabase {
    fun configureRealm()
    fun getAllDiaries(): Flow<RequestState>
}