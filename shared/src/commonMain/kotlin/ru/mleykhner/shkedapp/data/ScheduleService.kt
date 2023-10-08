package ru.mleykhner.shkedapp.data

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm
import ru.mleykhner.shkedapp.data.local.models.toViewDataObject
import ru.mleykhner.shkedapp.data.models.LessonViewData
import ru.mleykhner.shkedapp.data.remote.HttpRoutes
import ru.mleykhner.shkedapp.data.remote.models.ScheduleDTO
import ru.mleykhner.shkedapp.data.remote.models.toRealmObject

class ScheduleService: KoinComponent {
    val client: HttpClient by inject()
    val realm: Realm by inject()

    fun getScheduleForDate(date: LocalDate, group: String): List<LessonViewData> {

        val schedule: ScheduleRealm = realm.query<ScheduleRealm>(
            "groupName == $0",
            group
        ).first().find() ?: return emptyList()

        val one = schedule.week.toList().flatMap { it.daysSchedules.toList() }.flatMap { it.lessons.toList() }.mapNotNull { it.toViewDataObject() }
        return one
//        val schedule: ScheduleRealm = realm.query<ScheduleRealm>(
//            "groupName == $0",
//            group
//        ).first().find() ?: return emptyList()
//        val weekday = schedule.week.firstOrNull { it.dayNumber == date.dayOfWeek.ordinal } ?: return emptyList()
//        val date = weekday.daysSchedules.firstOrNull { it.dates.contains(date.toString()) } ?: return emptyList()
//        return date.lessons.mapNotNull { it.toViewDataObject() }.toList()
    }

    suspend fun update(group: String) {
        client.webSocket(HttpRoutes.SCHEDULE) {
            Napier.v("Socket opened!")
            send(Frame.Text("{\"protocol\":\"json\",\"version\":1}\u001E"))
            Napier.v("Handshake sent! response: ${(incoming.receive() as? Frame.Text)?.readText() ?: ""}")
            send(Frame.Text("{\"arguments\":[\"$group\"],\"target\":\"GroupSchedule\",\"type\":1}\u001E"))
            while (true) {
                val result = try {
                    val json = incoming.receive() as? Frame.Text ?: continue
                    Json.decodeFromString<ScheduleResponseDTO>(json.readText().dropLast(1))
                } catch (e: Exception) {
                    Napier.e(e, message = {e.message ?: ""})
                    continue
                }
                Napier.v("Got response!")
                if (result.arguments.isNotEmpty()) {
                    val argument = result.arguments.first()
                    val schedule = argument.schedule?.toRealmObject() ?: return@webSocket
                    Napier.i(message = schedule.groupName)
                    realm.write {
                        copyToRealm(
                            ScheduleRealm().apply {
                                groupName = group
                                week = schedule.week
                            },
                            updatePolicy = UpdatePolicy.ALL
                        )
                    }
                    Napier.v("Schedule updated!")
                    break
                }
            }
            close(CloseReason(CloseReason.Codes.NORMAL, "Schedule Successfully Received"))
        }
    }
}

@Serializable
data class ScheduleResponseDTO(
    val type: Int,
    val target: String,
    val arguments: List<ScheduleArgumentDTO>
)

@Serializable
data class ScheduleArgumentDTO(
    val status: String,
    val schedule: ScheduleDTO?
)