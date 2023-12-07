package com.hcdisat.dairyapp.feature_home.domain.repository

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiaryState
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.feature_home.model.DiaryResult
import com.hcdisat.dairyapp.feature_home.toPresentationDiary
import com.hcdisat.dairyapp.presentation.components.extensions.toLocalDate
import com.hcdisat.dairyapp.presentation.components.extensions.toPresentationDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DiaryRepository {
    fun getAll(): Flow<DiaryResult>
}

class DiaryRepositoryImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : DiaryRepository {
    override fun getAll(): Flow<DiaryResult> =
        mongoRepository.getAllDiaries().map { state ->
            when (state) {
                is DomainDiaryState.Failed -> DiaryResult.Error(state.throwable)
                is DomainDiaryState.Completed -> {
                    state.diaries.groupBy { it.date }.map { (instant, diaries) ->
                        instant.toLocalDate().toPresentationDate() to
                                diaries.map { it.toPresentationDiary() }
                    }.toMap().let { DiaryResult.Loaded(it) }
                }
            }
        }
}