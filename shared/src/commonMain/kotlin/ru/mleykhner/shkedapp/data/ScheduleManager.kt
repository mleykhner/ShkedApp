package ru.mleykhner.shkedapp.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import ru.mleykhner.shkedapp.data.remote.HttpRoutes
import ru.mleykhner.shkedapp.data.remote.httpClient

class ScheduleManager(
    val client: HttpClient = httpClient()
) {

    suspend fun load(group: String) {
        val response = client.get(HttpRoutes.GROUPS) {
            url {
                appendPathSegments(group)
            }
            header("User-Id", "developer")
        }
    }

}