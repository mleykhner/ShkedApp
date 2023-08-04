package ru.mleykhner.shkedapp.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Android) {
    install(ContentNegotiation) {
        json()
    }
    config(this)
    engine {
        //connectTimeout = 100_000
    }
}