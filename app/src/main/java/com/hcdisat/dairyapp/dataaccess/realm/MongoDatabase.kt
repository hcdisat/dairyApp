package com.hcdisat.dairyapp.dataaccess.realm

import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import kotlinx.coroutines.flow.Flow

interface MongoDatabase {
    fun configureRealm()
    fun getAllDiaries(): Flow<Result<List<Diary>>>
    suspend fun getSingleDiary(entryId: String): Result<Diary>
    suspend fun saveDiary(diary: Diary): Result<Diary>
}