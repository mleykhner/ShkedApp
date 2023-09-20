package ru.mleykhner.shkedapp.data.remote.models

import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.LessonType
import ru.mleykhner.shkedapp.data.local.models.LessonRealm

@Serializable
data class LessonDTO(
    val name: String,
    val lecturer: String?,
    val ordinal: Int,
    val type: LessonType,
    val location: String
)

fun LessonDTO.toRealmObject(): LessonRealm {
    return LessonRealm().apply {
        name = this@toRealmObject.name
        lecturer = this@toRealmObject.lecturer
        ordinal = this@toRealmObject.ordinal
        type = this@toRealmObject.type.name
        location = this@toRealmObject.location
    }
}

