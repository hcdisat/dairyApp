package com.hcdisat.dairyapp.dataaccess.realm

import io.realm.kotlin.log.LogLevel
import javax.inject.Inject

data class QueryDetails(val query: String, val name: String, val logLevel: LogLevel = LogLevel.ALL)

interface QueryProvider {
    fun filterQuery(): QueryDetails
    fun getById(): QueryDetails
}

class QueryProviderImpl @Inject constructor() : QueryProvider {
    override fun filterQuery(): QueryDetails = QueryDetails(
        query = "ownerId == $0",
        name = "diary-list"
    )

    override fun getById(): QueryDetails = QueryDetails(
        query = "_id == $0",
        name = "diary-list"
    )
}