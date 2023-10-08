package ru.mleykhner.shkedapp.data.remote.models

import io.realm.kotlin.ext.toRealmList
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.local.models.DayScheduleRealm

@Serializable
data class DayScheduleDTO(
    val dates: List<LocalDate>,
    @SerialName("classes")
    val lessons: List<LessonDTO>,
    val hashSum: String
)

fun DayScheduleDTO.toRealmObject(): DayScheduleRealm {
    return DayScheduleRealm().apply {
        dates = this@toRealmObject.dates.map { it.toString() }.toRealmList()
        lessons = this@toRealmObject.lessons.map { it.toRealmObject() }.toRealmList()
        hashSum = this@toRealmObject.hashSum
    }
}