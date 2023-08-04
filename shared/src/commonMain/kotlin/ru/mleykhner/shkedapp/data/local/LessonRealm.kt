package ru.mleykhner.shkedapp.data.local

import io.realm.kotlin.types.RealmObject

class LessonRealm: RealmObject {
    val name: String = ""
    val teacher: String? = null
    val ordinal: Int = 0
    val type: String = ""
    val location: String = ""
}