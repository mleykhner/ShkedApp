package ru.mleykhner.shkedapp.data.remote.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class CheckProfileStateRequestDTO(
    val email: String?,
    val phone: String?
)
