package ru.mleykhner.shkedapp.data

import kotlinx.datetime.LocalDate
import ru.mleykhner.shkedapp.data.models.LessonViewData

interface ScheduleService {
    suspend fun refresh(group: String): ScheduleRefreshResult
    fun getScheduleByDate(group: String, date: LocalDate): List<LessonViewData>?
}

enum class ScheduleRefreshResult {
    REFRESHED, HAS_CHANGES, OFFLINE, FAILED
}