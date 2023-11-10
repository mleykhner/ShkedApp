package ru.mleykhner.shkedapp.di

import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.realm.kotlin.Realm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mleykhner.shkedapp.BuildKonfig
import ru.mleykhner.shkedapp.data.ScheduleService
import ru.mleykhner.shkedapp.data.ScheduleServiceImpl
import ru.mleykhner.shkedapp.data.remote.AuthService
import ru.mleykhner.shkedapp.data.remote.AuthServiceImpl
import ru.mleykhner.shkedapp.data.remote.TokensService
import ru.mleykhner.shkedapp.data.remote.TokensServiceImpl
import ru.mleykhner.shkedapp.data.remote.httpClient
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult
import ru.mleykhner.shkedapp.utils.realmConfig

val commonModule = module {
    singleOf<AuthService>(::AuthServiceImpl)
    singleOf<TokensService>(::TokensServiceImpl)
    singleOf<ScheduleService>(::ScheduleServiceImpl)
    single { Realm.open(realmConfig) }
    single { httpClient {
        engine {
            headers {
                append("X-Api-Key", BuildKonfig.apiKey)
            }
        }
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val tokenService: TokensService by getKoin().inject()
                    tokenService.getTokens()
                }
                refreshTokens {
                    val tokenService: TokensService by getKoin().inject()
                    val authService: AuthService by getKoin().inject()
                    val result = authService.refresh()
                    if (result == AuthResult.SUCCESS) {
                        return@refreshTokens tokenService.getTokens()
                    }
                    return@refreshTokens null
                }
            }
        }
    } }
}