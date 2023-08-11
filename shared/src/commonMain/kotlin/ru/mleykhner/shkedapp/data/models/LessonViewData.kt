package ru.mleykhner.shkedapp.data.models

import ru.mleykhner.shkedapp.data.LessonType

data class LessonViewData(
    val name: String,
    val lecturer: String?,
    val ordinal: Int,
    val type: LessonType,
    val location: String
)
