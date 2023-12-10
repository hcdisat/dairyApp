package com.hcdisat.dairyapp.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.core.extensions.toLocalDate
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import com.hcdisat.dairyapp.domain.extensions.toDomainDiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MongoRepositoryImpl @Inject constructor(
    private val mongoDB: MongoDatabase
) : MongoRepository {
    override fun getAllDiaries(): Flow<Result<Map<LocalDate, List<DomainDiary>>>> =
        mongoDB.getAllDiaries().map { result ->
            result.mapCatching { Result.success(it.toDiaryMap()) }
                .getOrElse { Result.failure(it) }
        }

    private fun List<Diary>.toDiaryMap(): Map<LocalDate, List<DomainDiary>> =
        groupBy { it.date.toLocalDate() }
            .map { map -> map.key to map.value.map { it.toDomainDiary() } }
            .toMap()
}