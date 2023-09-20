package ru.mleykhner.shkedapp.data.remote

import io.ktor.client.plugins.auth.providers.BearerTokens

interface TokensService {
    fun updateTokens(tokens: BearerTokens)
    fun getTokens(): BearerTokens?
    fun deleteTokens()
}