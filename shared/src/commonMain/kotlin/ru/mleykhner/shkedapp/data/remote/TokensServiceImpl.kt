package ru.mleykhner.shkedapp.data.remote

import com.liftric.kvault.KVault
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class TokensServiceImpl: TokensService, KoinComponent {
    private val kVault: KVault by inject { parametersOf("authInfo") }
    override fun updateTokens(tokens: BearerTokens) {
        kVault.set("accessToken", tokens.accessToken)
        kVault.set("refreshToken", tokens.refreshToken)
    }

    override fun getTokens(): BearerTokens? {
        val accessToken = kVault.string("accessToken") ?: return null
        val refreshToken = kVault.string("refreshToken") ?: return null
        return BearerTokens(accessToken, refreshToken)
    }

    override fun deleteTokens() {
        kVault.deleteObject("accessToken")
        kVault.deleteObject("refreshToken")
    }
}