package ru.mleykhner.shkedapp.data.remote

import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult
import ru.mleykhner.shkedapp.data.remote.models.auth.SignUpDTO

interface AuthService {
    suspend fun signIn(email: String, password: String): List<AuthResult>
    suspend fun signUp(dto: SignUpDTO): List<AuthResult>
    suspend fun refresh(): AuthResult
    suspend fun logout(): AuthResult
    suspend fun logoutFromAll(): AuthResult
}