package com.hcdisat.dairyapp.dataaccess.realm.model

import java.time.LocalDate

sealed interface RequestState {
    data class Success(val diaries: Map<LocalDate, List<Diary>>) : RequestState
    data class Error(val throwable: Throwable) : RequestState
}