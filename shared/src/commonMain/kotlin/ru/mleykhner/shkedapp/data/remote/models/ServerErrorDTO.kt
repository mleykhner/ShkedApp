package ru.mleykhner.shkedapp.data.remote.models

import kotlinx.serialization.Serializable
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult

@Serializable
data class ServerErrorDTO(
    val errorCode: Int,
    val errorMessage: String
)

fun ServerErrorDTO.toAuthResult(): AuthResult {
    return when(this.errorCode) {
        0 -> AuthResult.EMAIL_OCCUPIED
        1 -> AuthResult.SUCCESS
        2, 3, 6, 8, 9 -> AuthResult.WRONG_CREDENTIALS
        4 -> AuthResult.FAILED
        5 -> AuthResult.INVALID_REFRESH_TOKEN
        7 -> AuthResult.INVALID_GROUP
        else -> AuthResult.FAILED
    }
}
