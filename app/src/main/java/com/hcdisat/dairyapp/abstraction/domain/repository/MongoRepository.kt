package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MongoRepository {
    fun getAllDiaries(): Flow<Result<Map<LocalDate, List<DomainDiary>>>>
    fun getSingleDiary(entryId: String): Result<Pair<LocalDate, DomainDiary>>
}