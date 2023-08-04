package ru.mleykhner.shkedapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LessonType {
    @SerialName("lecture")
    LECTURE,
    @SerialName("practical")
    PRACTICAL,
    @SerialName("laboratory")
    LABORATORY,
    @SerialName("test")
    TEST,
    @SerialName("exam")
    EXAM
}