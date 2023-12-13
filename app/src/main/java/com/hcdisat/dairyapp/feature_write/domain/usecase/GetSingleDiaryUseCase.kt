package com.hcdisat.dairyapp.feature_write.domain.usecase

import com.hcdisat.dairyapp.abstraction.domain.model.DomainDiary
import com.hcdisat.dairyapp.abstraction.domain.repository.MongoRepository
import com.hcdisat.dairyapp.feature_write.model.DiaryEntryState
import com.hcdisat.dairyapp.feature_write.model.EntryScreenState
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.toMutableDairyPresentationDate
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDate
import com.hcdisat.dairyapp.presentation.extensions.toPresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.update
import java.time.LocalDate
import javax.inject.Inject

interface GetSingleDiaryUseCase {
    operator fun invoke(entryId: String): Result<DiaryEntryState>
}

class GetSingleDiaryUseCaseImpl @Inject constructor(
    private val mongoRepository: MongoRepository
) : GetSingleDiaryUseCase {
    override fun invoke(entryId: String): Result<DiaryEntryState> =
        mongoRepository.getSingleDiary(entryId).mapCatching { (localDate, domainDiary) ->
            DiaryEntryState(
                diaryEntry = mapResult(localDate, domainDiary),
                screenState = EntryScreenState.READY
            )
        }

    private fun mapResult(localDate: LocalDate, domainDiary: DomainDiary): PresentationDiary {
        return domainDiary.toPresentationDiary().update {
            date = localDate.toPresentationDate().toMutableDairyPresentationDate()
        }
    }
}