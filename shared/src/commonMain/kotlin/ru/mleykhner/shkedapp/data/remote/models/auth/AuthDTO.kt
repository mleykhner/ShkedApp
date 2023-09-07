package ru.mleykhner.shkedapp.data.remote.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthDTO(
    val accessToken: String,
    val refreshToken: String,
    val uuid: String
)