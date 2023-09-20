package ru.mleykhner.shkedapp.data.local.models

import io.github.aakira.napier.Napier
import io.realm.kotlin.types.EmbeddedRealmObject
import ru.mleykhner.shkedapp.data.LessonType
import ru.mleykhner.shkedapp.data.models.LessonViewData

class LessonRealm: EmbeddedRealmObject {
    var name: String = ""
    var lecturer: String? = null
    var ordinal: Int = 0
    var type: String = ""
    var location: String = ""
}

fun LessonRealm.toViewDataObject(): LessonViewData? {
    return try {
        LessonViewData(
            name = this.name,
            lecturer = this.lecturer,
            ordinal = this.ordinal,
            type = LessonType.valueOf(this.type),
            location = this.location
        )
    } catch (e: IllegalArgumentException) {
        Napier.e("LessonRealm.toViewDataObject(): No such LessonType - ${this.type}")
        null
    }
}