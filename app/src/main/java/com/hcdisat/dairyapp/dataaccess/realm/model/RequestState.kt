package com.hcdisat.dairyapp.dataaccess.realm.model

sealed interface RequestState {
    data class Success(val diaries: List<Diary>) : RequestState
    data class Error(val throwable: Throwable) : RequestState
}