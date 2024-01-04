package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.feature_write.model.DiaryEntryState
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.ui.extensions.toPresentationDiary
import javax.inject.Inject

interface GetSingleDiaryUseCase {
    suspend operator fun invoke(entryId: String): Result<DiaryEntryState>
}

class GetSingleDiaryUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : GetSingleDiaryUseCase {
    override suspend fun invoke(entryId: String): Result<DiaryEntryState> =
        mongoRepository.getSingleDiary(entryId).mapCatching { domainDiary ->
            DiaryEntryState(
                diaryEntry = domainDiary.toPresentationDiary(),
                screenState = EntryScreenState.Ready
            )
        }
}