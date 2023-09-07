package ru.mleykhner.shkedapp.data.local

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet

class UserRealm: RealmObject {
    var userId: String = ""
    var fullName: String = ""
    var email: String = ""
    var group: String = ""
    var friendGroups: RealmSet<String> = realmSetOf()
}