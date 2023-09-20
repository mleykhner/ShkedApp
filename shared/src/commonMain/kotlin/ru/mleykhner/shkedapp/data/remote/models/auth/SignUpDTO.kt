package ru.mleykhner.shkedapp.data.remote.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignUpDTO(
    val fullName: String,
    val email: String,
    val password: String,
    val group: String
)
