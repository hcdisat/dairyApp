package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MongoRepository {
    fun getAllDiaries(): Flow<Result<Map<LocalDateTime, List<DomainDiary>>>>
    suspend fun getSingleDiary(entryId: String): Result<DomainDiary>
}