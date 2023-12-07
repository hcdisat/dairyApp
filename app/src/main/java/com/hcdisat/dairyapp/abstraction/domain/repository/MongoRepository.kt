package com.hcdisat.dairyapp.abstraction.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiaryState
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun getAllDiaries(): Flow<DomainDiaryState>
}