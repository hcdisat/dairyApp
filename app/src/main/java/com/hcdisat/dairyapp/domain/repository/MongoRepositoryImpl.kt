package com.hcdisat.dairyapp.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.core.extensions.toLocalDateTime
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import com.hcdisat.dairyapp.domain.extensions.toDomainDiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MongoRepositoryImpl @Inject constructor(
    private val mongoDB: MongoDatabase
) : MongoRepository {
    override fun getAllDiaries(): Flow<Result<Map<LocalDateTime, List<DomainDiary>>>> =
        mongoDB.getAllDiaries().map { result -> result.mapCatching { it.toDiaryMap() } }

    override suspend fun getSingleDiary(entryId: String): Result<DomainDiary> =
        mongoDB.getSingleDiary(entryId).mapCatching { it.toDomainDiary() }

    private fun List<Diary>.toDiaryMap(): Map<LocalDateTime, List<DomainDiary>> =
        groupBy { it.date.toLocalDateTime() }
            .map { map -> map.key to map.value.map { it.toDomainDiary() } }
            .toMap()
}