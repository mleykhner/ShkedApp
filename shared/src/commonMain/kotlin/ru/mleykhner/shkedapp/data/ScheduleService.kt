package ru.mleykhner.shkedapp.data

import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm
import ru.mleykhner.shkedapp.data.models.LessonViewData

interface ScheduleService {
    suspend fun refresh(group: String): ScheduleRefreshResult
    fun getScheduleByDate(group: String, date: LocalDate): List<LessonViewData>?
    fun getScheduleAsFlow(group: String): Flow<SingleQueryChange<ScheduleRealm>>
}

enum class ScheduleRefreshResult {
    REFRESHED, HAS_CHANGES, OFFLINE, FAILED
}