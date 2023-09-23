package ru.mleykhner.shkedapp.di

import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.realm.kotlin.Realm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
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
    single { Realm.open(realmConfig) }
    single { httpClient {
        install(ContentNegotiation) {
            json(
//                Json {
//                    serializersModule = SerializersModule {
//                        contextual(List<LocalDate>::class, ListSerializer(LocalDateIso8601Serializer))
//                        contextual(LocalDate::class, LocalDateIso8601Serializer)
//                    }
//                }
            )
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
    //single { logging() }
}