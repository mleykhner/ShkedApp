package ru.mleykhner.shkedapp.data.local.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class DayScheduleRealm: EmbeddedRealmObject {
    var dates: RealmList<String> = realmListOf()
    var lessons: RealmList<LessonRealm> = realmListOf()
    var hashSum: String = ""
}