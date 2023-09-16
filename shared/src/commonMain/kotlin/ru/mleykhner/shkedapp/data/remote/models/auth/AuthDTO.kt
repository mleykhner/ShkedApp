package ru.mleykhner.shkedapp.data.remote.models.auth

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.Serializable

@Serializable
data class AuthDTO(
    val accessToken: String,
    val refreshToken: String,
    val uuid: String
)

fun AuthDTO.toBearerTokens(): BearerTokens {
    return BearerTokens(accessToken, refreshToken)
}
