package ru.mleykhner.shkedapp.data.local

import io.realm.kotlin.types.RealmObject

class LessonRealm: RealmObject {
    var name: String = ""
    var teacher: String? = null
    var ordinal: Int = 0
    var type: String = ""
    var location: String = ""
}