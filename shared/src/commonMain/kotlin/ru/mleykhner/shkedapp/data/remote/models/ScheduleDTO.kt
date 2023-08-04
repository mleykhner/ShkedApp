package ru.mleykhner.shkedapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDTO(
    val groupName: String,
    val schedule: GroupScheduleDTO
)

@Serializable
data class GroupScheduleDTO(
    val week: List<WeekdayScheduleDTO>
)

@Serializable
data class WeekdayScheduleDTO(
    val daysSchedules: List<DaysScheduleDTO>,
    val dayNumber: Int
)

@Serializable
data class DaysScheduleDTO(
    val dates: List<String>,
    var classes: List<LessonDTO>
)