package com.hcdisat.dairyapp.feature_home.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.feature_home.model.DiaryResult
import com.hcdisat.dairyapp.feature_home.toPresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetDiariesUseCase {
    operator fun invoke(): Flow<Result<DiaryResult>>
}

class GetDiariesUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : GetDiariesUseCase {
    override operator fun invoke(): Flow<Result<DiaryResult>> =
        mongoRepository.getAllDiaries().map { result ->
            result.mapCatching { domainMap ->
                val mapped = domainMap.map { (key, value) ->
                    key.toPresentationDate() to value.map { it.toPresentationDiary() }
                }.toMap()
                Result.success(mapped)
            }.getOrElse {
                Result.failure(it)
            }
        }
}