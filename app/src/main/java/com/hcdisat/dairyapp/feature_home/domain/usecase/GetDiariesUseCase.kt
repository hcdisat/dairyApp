package com.hcdisat.dairyapp.feature_home.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetDiariesUseCase {
    operator fun invoke(): Flow<Result<List<PresentationDiary>>>
}

class GetDiariesUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : GetDiariesUseCase {
    override operator fun invoke(): Flow<Result<List<PresentationDiary>>> =
        mongoRepository.getAllDiaries().map { result ->
            result.mapCatching { diaries -> diaries.map { it.toPresentationDiary() } }
        }
}