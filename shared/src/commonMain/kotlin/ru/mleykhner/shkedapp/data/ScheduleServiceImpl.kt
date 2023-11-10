package ru.mleykhner.shkedapp.data

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm
import ru.mleykhner.shkedapp.data.local.models.toViewDataObject
import ru.mleykhner.shkedapp.data.models.LessonViewData
import ru.mleykhner.shkedapp.data.remote.HttpRoutes
import ru.mleykhner.shkedapp.data.remote.models.ScheduleDTO
import ru.mleykhner.shkedapp.data.remote.models.toRealmObject

class ScheduleServiceImpl: ScheduleService, KoinComponent {
    val client: HttpClient by inject()
    val realm: Realm by inject()

    override fun getScheduleByDate(group: String, date: LocalDate): List<LessonViewData>? {
        val schedule = realm.query<ScheduleRealm>("groupName == $0", group).first().find() ?: return null
        val weekdaySchedule = schedule.week.query("dayNumber == $0", date.dayOfWeek.ordinal + 1).first().find() ?: return emptyList()
        val dateSchedule = weekdaySchedule.daysSchedules.firstOrNull { it.dates.contains(date.toString()) } ?: return emptyList()
        return dateSchedule.lessons.toList().mapNotNull { it.toViewDataObject() }
    }

    override suspend fun refresh(group: String): ScheduleRefreshResult {
        val response = try {
            client.get(HttpRoutes.GROUPS)
        } catch (e: Exception) {
            Napier.e("Refresh failed: ", e)
            return ScheduleRefreshResult.FAILED
        }
        if (response.status.value in 200..299) {
            Napier.v("Got response")
            return try {
                val result: ScheduleDTO = response.body()
                val realmObject = result.toRealmObject()
                realm.write {
                    copyToRealm(realmObject, updatePolicy = UpdatePolicy.ALL)
                }
                ScheduleRefreshResult.REFRESHED
            } catch (e: NoTransformationFoundException) {
                Napier.e("Serialization error: ", e)
                ScheduleRefreshResult.FAILED
            }
        }
        return ScheduleRefreshResult.FAILED
    }
}