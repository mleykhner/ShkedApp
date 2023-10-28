package ru.mleykhner.shkedapp.data.remote.models.auth

import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.remote.ProfileState

@Serializable
enum class ProfileStateDTO {
    PASSWORD, PASSKEY, NOT_REGISTERED
}

fun ProfileStateDTO.toProfileState(): ProfileState {
    return when (this) {
        ProfileStateDTO.PASSWORD -> ProfileState.Password
        ProfileStateDTO.PASSKEY -> ProfileState.Passkey
        ProfileStateDTO.NOT_REGISTERED -> ProfileState.NotRegistered
    }
}
