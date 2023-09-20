package ru.mleykhner.shkedapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.websocket.WebSockets


actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    install(WebSockets) {
        pingInterval = 6_000
    }
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    config(this)
}