package com.hcdisat.dairyapp.dataaccess.realm

import io.realm.kotlin.log.LogLevel
import javax.inject.Inject

data class QueryDetails(val query: String, val name: String, val logLevel: LogLevel = LogLevel.ALL)

interface QueryProvider {
    fun initialSubscriptionQuery(): QueryDetails
}

class QueryProviderImpl @Inject constructor() : QueryProvider {
    override fun initialSubscriptionQuery(): QueryDetails = QueryDetails(
        query = "ownerId == $0",
        name = "All Diaries"
    )
}