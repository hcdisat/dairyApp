@file:Suppress("PropertyName")

package com.hcdisat.dairyapp.dataaccess.realm.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var ownerId: String = ""
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    var date: RealmInstant = RealmInstant.now()
    var mood: String = "Neutral"
}