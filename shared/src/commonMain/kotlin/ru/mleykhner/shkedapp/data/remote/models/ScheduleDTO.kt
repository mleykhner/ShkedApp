package ru.mleykhner.shkedapp.data.remote.models

import io.realm.kotlin.ext.toRealmList
import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm

@Serializable
data class ScheduleDTO(
    val groupName: String,
    val schedule: List<WeekdayDTO>
)

fun ScheduleDTO.toRealmObject(): ScheduleRealm {
    return ScheduleRealm().apply {
        groupName = this@toRealmObject.groupName
        schedule = this@toRealmObject.schedule.map { it.toRealmObject() }.toRealmList()
    }
}