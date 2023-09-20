package ru.mleykhner.shkedapp.data.local.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ScheduleRealm: RealmObject {
    @PrimaryKey
    var groupName: String = ""
    var schedule: RealmList<WeekdayRealm> = realmListOf()
}