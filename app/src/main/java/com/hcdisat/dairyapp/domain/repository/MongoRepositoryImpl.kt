package com.hcdisat.dairyapp.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import com.hcdisat.dairyapp.dataaccess.realm.model.RealmGenericException
import com.hcdisat.dairyapp.domain.extensions.toDiary
import com.hcdisat.dairyapp.domain.extensions.toDomainDiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MongoRepositoryImpl @Inject constructor(
    private val mongoDB: MongoDatabase
) : MongoRepository {
    override fun getAllDiaries(): Flow<Result<List<DomainDiary>>> =
        mongoDB.getAllDiaries().map { result ->
            result.mapCatching { diaries ->
                diaries.map { it.toDomainDiary() }
            }
        }

    override suspend fun getSingleDiary(entryId: String): Result<DomainDiary> =
        mongoDB.getSingleDiary(entryId).mapCatching { it.toDomainDiary() }

    override suspend fun saveDiary(domainDiary: DomainDiary): Result<DomainDiary> {
        val maybeDiary = domainDiary.toDiary().getOrNull()

        return maybeDiary?.let { diary ->
            mongoDB.saveDiary(diary).mapCatching { it.toDomainDiary() }
        } ?: throw RealmGenericException(Exception())

    }
}