package com.hcdisat.dataaccess.realm

import io.realm.kotlin.log.LogLevel
import javax.inject.Inject

data class QueryDetails(val query: String, val name: String, val logLevel: LogLevel = LogLevel.ALL)

interface QueryProvider {
    fun filterQuery(): QueryDetails
    fun getById(): QueryDetails
    fun getByIdAndOwnerId(): QueryDetails
}

class QueryProviderImpl @Inject constructor() : QueryProvider {
    override fun filterQuery(): QueryDetails = QueryDetails(
        query = "ownerId == $0",
        name = "diary-list"
    )

    override fun getById(): QueryDetails = QueryDetails(
        query = "_id == $0",
        name = "single-diary"
    )

    override fun getByIdAndOwnerId(): QueryDetails = QueryDetails(
        query = "_id == $0 && ownerId == $1",
        name = "single-diary-and-owner"
    )
}