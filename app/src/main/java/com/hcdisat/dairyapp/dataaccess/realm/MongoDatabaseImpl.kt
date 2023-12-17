package com.hcdisat.dairyapp.dataaccess.realm

import android.util.Log
import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import com.hcdisat.dairyapp.dataaccess.realm.model.RealmGenericException
import com.hcdisat.dairyapp.dataaccess.realm.model.UserNotAuthenticatedException
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class MongoDatabaseImpl @Inject constructor(
    private val realmApp: App,
    private val queryProvider: QueryProvider
) : MongoDatabase {
    private lateinit var realm: Realm
    private val user = realmApp.currentUser

    override fun configureRealm() {
        realmApp.currentUser?.let(::config)
    }

    override fun getAllDiaries(): Flow<Result<List<Diary>>> {
        return if (!isUserLoggedIn()) {
            flowOf(Result.failure(UserNotAuthenticatedException()))
        } else {
            runCatching {
                realm.query<Diary>()
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { results ->
                        Result.success(results.list.toList())
                    }
            }.getOrElse {
                Log.e("MongoDatabaseImpl", "${it.message}", it)
                flowOf(Result.failure(RealmGenericException(it)))
            }
        }
    }

    override suspend fun getSingleDiary(entryId: String): Result<Diary> {
        if (!isUserLoggedIn()) return Result.failure(UserNotAuthenticatedException())
        val query = queryProvider.getById().query
        val id = ObjectId.invoke(entryId)

        return runCatching {
            realm.query<Diary>(query = query, id).find().first()
        }.mapCatching { it }
    }

    override suspend fun saveDiary(diary: Diary): Result<Diary> = runCatching {
        checkUser(user)
        realm.write {
            val addedDiary = copyToRealm(diary.apply { ownerId = user.id })
            addedDiary
        }
    }

    private fun config(user: User) {
        val filterQuery = queryProvider.filterQuery()
        val config = SyncConfiguration.Builder(
            user = user,
            schema = setOf(Diary::class)
        ).initialSubscriptions { subscription ->
            add(
                query = subscription.query<Diary>(filterQuery.query, user.id),
                name = filterQuery.name
            )
        }.log(filterQuery.logLevel).build()

        realm = Realm.open(config)
    }

    private fun isUserLoggedIn() = user != null

    @OptIn(ExperimentalContracts::class)
    private fun checkUser(user: User?) {
        contract {
            returns() implies (user != null)
        }

        if (user == null) throw UserNotAuthenticatedException()
    }
}