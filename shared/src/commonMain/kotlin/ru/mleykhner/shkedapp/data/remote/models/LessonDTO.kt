package ru.mleykhner.shkedapp.data.remote.models

import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.LessonType

@Serializable
data class LessonDTO(
    val name: String,
    val teacher: String?,
    val ordinal: Int,
    val type: LessonType,
    val location: String
)
