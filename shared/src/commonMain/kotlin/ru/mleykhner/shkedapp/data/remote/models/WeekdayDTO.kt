package ru.mleykhner.shkedapp.data.remote.models

import io.realm.kotlin.ext.toRealmList
import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.local.models.WeekdayRealm

@Serializable
data class WeekdayDTO(
    val daysSchedules: List<DayScheduleDTO>,
    val dayNumber: Int
)

fun WeekdayDTO.toRealmObject(): WeekdayRealm {
    return WeekdayRealm().apply {
        daysSchedules = this@toRealmObject.daysSchedules.map { it.toRealmObject() }.toRealmList()
        dayNumber = this@toRealmObject.dayNumber
    }
}