package com.hcdisat.dairyapp.feature_home.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.ui.extensions.toPresentationDiary
import com.hcdisat.ui.model.PresentationDiary
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