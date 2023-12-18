package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun getAllDiaries(): Flow<Result<List<DomainDiary>>>
    suspend fun getSingleDiary(entryId: String): Result<DomainDiary>
    suspend fun saveDiary(domainDiary: DomainDiary): Result<DomainDiary>
}