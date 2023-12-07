package com.hcdisat.dairyapp.dataaccess.realm

import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import com.hcdisat.dairyapp.dataaccess.realm.model.RequestState
import com.hcdisat.dairyapp.dataaccess.realm.model.UserNotAuthenticatedException
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MongoDatabaseImpl @Inject constructor(
    private val realmApp: App,
    queryProvider: QueryProvider
) : MongoDatabase {
    private lateinit var realm: Realm
    private val filterQuery = queryProvider.filterQuery()

    override fun configureRealm() {
        realmApp.currentUser?.let(::config)
    }

    override fun getAllDiaries(): Flow<RequestState> = flow {
        val user = realmApp.currentUser
        if (user == null) {
            emit(RequestState.Error(UserNotAuthenticatedException()))
            return@flow
        }

        runCatching {
            realm.query<Diary>(query = filterQuery.query, user.id)
                .sort(property = "date", sortOrder = Sort.DESCENDING)
                .asFlow()
        }.onFailure {
            it.printStackTrace()
            emit(RequestState.Error(it))
        }
    }

    private fun config(user: User) {
        val config = SyncConfiguration.Builder(user, setOf(Diary::class))
            .initialSubscriptions { subscription ->
                add(
                    query = subscription.query(filterQuery.query, user.id),
                    name = filterQuery.name
                )
            }.log(filterQuery.logLevel).build()

        realm = Realm.open(config)
    }
}