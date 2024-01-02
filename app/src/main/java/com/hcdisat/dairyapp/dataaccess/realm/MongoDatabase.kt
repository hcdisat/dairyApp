package com.hcdisat.dairyapp.dataaccess.realm

import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface MongoDatabase {
    fun configureRealm()
    fun getAllDiaries(): Flow<Result<List<Diary>>>
    suspend fun getSingleDiary(entryId: String): Result<Diary>
    suspend fun saveDiary(diary: Diary): Result<Diary>
    suspend fun updateDiary(diary: Diary): Result<Diary>
    suspend fun deleteDiary(entryId: ObjectId): Result<Boolean>
    suspend fun deleteAllDiaries(): Result<Unit>
}