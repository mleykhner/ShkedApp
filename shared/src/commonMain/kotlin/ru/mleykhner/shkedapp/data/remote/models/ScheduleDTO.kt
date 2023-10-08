package ru.mleykhner.shkedapp.data.remote.models

import io.realm.kotlin.ext.toRealmList
import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm

@Serializable
data class ScheduleDTO(
    val groupName: String,
    val week: List<WeekdayDTO>
)

fun ScheduleDTO.toRealmObject(): ScheduleRealm {
    return ScheduleRealm().apply {
        groupName = this@toRealmObject.groupName
        week = this@toRealmObject.week.map { it.toRealmObject() }.toRealmList()
    }
}