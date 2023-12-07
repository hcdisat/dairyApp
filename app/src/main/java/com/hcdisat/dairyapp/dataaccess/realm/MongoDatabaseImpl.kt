package com.hcdisat.dairyapp.dataaccess.realm

import com.hcdisat.dairyapp.dataaccess.realm.model.Diary
import com.hcdisat.dairyapp.dataaccess.realm.repository.MongoDatabase
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import javax.inject.Inject

class MongoDatabaseImpl @Inject constructor(
    private val realmApp: App,
    private val queryProvider: QueryProvider
) : MongoDatabase {
    private lateinit var realm: Realm

    override fun configureRealm() {
        realmApp.currentUser?.let(::config)
    }

    private fun config(user: User) {
        val configQuery = queryProvider.initialSubscriptionQuery()
        val config = SyncConfiguration.Builder(user, setOf(Diary::class))
            .initialSubscriptions { subscription ->
                add(
                    query = subscription.query(configQuery.query, user.id),
                    name = configQuery.name
                )
            }.log(configQuery.logLevel).build()

        realm = Realm.open(config)
    }
}