package com.hcdisat.dairyapp.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiaryState
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.dataaccess.realm.MongoDatabase
import com.hcdisat.dairyapp.dataaccess.realm.model.RequestState
import com.hcdisat.dairyapp.domain.extensions.toDomainDiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MongoRepositoryImpl @Inject constructor(
    private val mongoDB: MongoDatabase
) : MongoRepository {
    override fun getAllDiaries(): Flow<DomainDiaryState> =
        mongoDB.getAllDiaries().map { requestState ->
            when (requestState) {
                is RequestState.Error -> DomainDiaryState.Failed(requestState.throwable)
                is RequestState.Success -> DomainDiaryState.Completed(
                    requestState.diaries.map { it.toDomainDiary() }
                )
            }
        }
}