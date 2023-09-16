package ru.mleykhner.shkedapp.data.remote.models.auth

enum class AuthResult {
    SUCCESS,
    WRONG_CREDENTIALS,
    EMAIL_OCCUPIED,
    INVALID_REFRESH_TOKEN,
    SERIALIZATION_ERROR,
    INVALID_GROUP,
    TIMEOUT,
    CONNECTION_ERROR,
    FAILED
}