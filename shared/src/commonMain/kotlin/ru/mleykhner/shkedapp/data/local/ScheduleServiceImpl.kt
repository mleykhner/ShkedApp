package ru.mleykhner.shkedapp.data.local

import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm
import ru.mleykhner.shkedapp.data.local.models.toViewDataObject
import ru.mleykhner.shkedapp.data.models.LessonViewData

class ScheduleServiceImpl: ScheduleService, KoinComponent {
    val realm: Realm by inject()
    val client: HttpClient by inject()
    override suspend fun refresh(group: String, progressHandler: (Int) -> Unit): ScheduleRefreshResult {

        TODO("Not yet implemented")
    }

    override fun getScheduleByDate(group: String, date: LocalDate): List<LessonViewData>? {
        val schedule = realm.query<ScheduleRealm>("groupName == $0", group).first().find() ?: return null
        val weekdaySchedule = schedule.schedule.query("dayNumber == $0", date.dayOfWeek.ordinal + 1).first().find() ?: return emptyList()
        val dateSchedule = weekdaySchedule.daySchedule.firstOrNull { it.dates.contains(date.toString()) } ?: return emptyList()
        return dateSchedule.lessons.toList().mapNotNull { it.toViewDataObject() }
    }
}

