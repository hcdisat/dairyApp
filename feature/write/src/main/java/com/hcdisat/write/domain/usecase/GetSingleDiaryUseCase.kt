package com.hcdisat.write.domain.usecase

import com.hcdisat.abstraction.domain.repository.MongoRepository
import com.hcdisat.ui.extensions.toPresentationDiary
import com.hcdisat.write.model.DiaryEntryState
import com.hcdisat.write.model.EntryScreenState
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