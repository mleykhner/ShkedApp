package ru.mleykhner.shkedapp.data.remote

import com.liftric.kvault.KVault
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import ru.mleykhner.shkedapp.data.remote.models.ServerErrorDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult
import ru.mleykhner.shkedapp.data.remote.models.auth.SignUpDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.toBearerTokens
import ru.mleykhner.shkedapp.data.remote.models.toAuthResult

interface PreferencesService {
    fun updateTokens(tokens: BearerTokens)
    fun getTokens(): BearerTokens?
    fun deleteTokens()
}

class PreferencesServiceImpl: PreferencesService, KoinComponent {
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

class AuthServiceImpl: AuthService, KoinComponent {
    private val client: HttpClient by inject()
    //private val kvault: KVault by inject()
    private val prefs: PreferencesService by inject()
    //private val log: KmLog by inject()

    //TODO: Разделить ответственность
    //TODO: Перенести строки в отдельный файл

    override suspend fun signIn(email: String, password: String): List<AuthResult> {
        Napier.v("Signing In...")
        val response = try {
            client.get(HttpRoutes.AUTH_SIGN_IN) {
                url {
                    parameters.append("email", email)
                    parameters.append("password", password)
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            Napier.e("Sign In FAILED: Timeout", throwable = e)
            return listOf(AuthResult.TIMEOUT)
        } catch (e: Exception) {
            Napier.e("Sign In FAILED: ", throwable = e)
            //log.e(e, msg = { e.cause })
            return listOf(AuthResult.CONNECTION_ERROR, AuthResult.FAILED)
        }
        if (response.status.value in 200..299) {
            Napier.v("Sign In response OK, code: ${response.status.value}")
            return try {
                val result: AuthDTO = response.body()
//                kvault.set("refreshToken", result.refreshToken)
//                kvault.set("accessToken", result.accessToken)
                prefs.updateTokens(result.toBearerTokens())
                Napier.v("Sign In tokens refreshed")
                listOf(AuthResult.SUCCESS)

            } catch (e: NoTransformationFoundException) {
                Napier.e("Sign In FAILED. Serialization error: ", throwable = e)
                listOf(AuthResult.SERIALIZATION_ERROR)
            }
        }
        if (response.status.value in 400..499) {
            Napier.e("Sign In response FAILED, code: ${response.status.value}")
            return try {
                val errors: List<ServerErrorDTO> = response.body()
                errors.map {
                    it.toAuthResult()
                }.also { results ->
                    Napier.e("Sign In FAILED. Server reasons: ${results.joinToString { it.name }}")
                }
            } catch (e: NoTransformationFoundException) {
                Napier.e("Sign In FAILED. Unable to convert to readable error: ", e)
                listOf(AuthResult.SERIALIZATION_ERROR)
            }
        }
        Napier.e("Sign In FAILED. Undefined error")
        return listOf(AuthResult.FAILED)
    }

    override suspend fun signUp(dto: SignUpDTO): List<AuthResult> {
        val response = try {
            client.post(HttpRoutes.AUTH_SIGN_UP) {
                setBody(dto)
            }
        } catch (e: HttpRequestTimeoutException) {
            return listOf(AuthResult.TIMEOUT)
        }
        if (response.status.value in 200..299) {
            return try {
                val result: AuthDTO = response.body()
                // TODO: Улучшить логику авторизации
//                kvault.set("refreshToken", result.refreshToken)
//                kvault.set("accessToken", result.accessToken)
                prefs.updateTokens(result.toBearerTokens())
                listOf(AuthResult.SUCCESS)
            } catch (e: NoTransformationFoundException) {
                listOf(AuthResult.SERIALIZATION_ERROR)
            }
        }
        if (response.status.value in 400..499) {
            return try {
                val errors: List<ServerErrorDTO> = response.body()
                errors.map {
                    //TODO: Добавить кроссплатформенный логгер
                    //Log.e("AuthService", "SignUp error. Code: ")
                    it.toAuthResult()
                }
            } catch (e: NoTransformationFoundException) {
                listOf(AuthResult.SERIALIZATION_ERROR)
            }
        }
        return listOf(AuthResult.FAILED)
    }

    override suspend fun refresh(): AuthResult {
        val token = prefs.getTokens()?.refreshToken ?: return AuthResult.INVALID_REFRESH_TOKEN
        val response = try {
            client.get(HttpRoutes.AUTH_REFRESH) {
                url {
                    parameters.append("refreshToken", token)
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            return AuthResult.TIMEOUT
        }
        if (response.status.value in 200..299) {
            // TODO: Добавить логику авторизации
            return AuthResult.SUCCESS
        }
        if (response.status.value in 400..499) {
            return try {
                val errors: List<ServerErrorDTO> = response.body()
                return errors.firstOrNull()?.toAuthResult() ?: AuthResult.FAILED
            } catch (e: NoTransformationFoundException) {
                AuthResult.SERIALIZATION_ERROR
            }
        }
        return AuthResult.FAILED
    }

    override suspend fun logout(): AuthResult {
        val token = prefs.getTokens()?.refreshToken ?: return AuthResult.INVALID_REFRESH_TOKEN
        val response = try {
            client.delete(HttpRoutes.AUTH_LOGOUT) {
                url {
                    parameters.append("refreshToken", token)
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            return AuthResult.TIMEOUT
        }
        if (response.status.value in 200..299) {

//            kvault.deleteObject("refreshToken")
//            kvault.deleteObject("accessToken")
            prefs.deleteTokens()
            return AuthResult.SUCCESS
        }
        if (response.status.value in 400..499) {
            return try {
                val errors: List<ServerErrorDTO> = response.body()
                return errors.firstOrNull()?.toAuthResult() ?: AuthResult.FAILED
            } catch (e: NoTransformationFoundException) {
                AuthResult.SERIALIZATION_ERROR
            }
        }
        return AuthResult.FAILED
    }

    override suspend fun logoutFromAll(): AuthResult {
        val response = try {
            client.delete(HttpRoutes.AUTH_LOGOUT_FROM_ALL)
        } catch (e: HttpRequestTimeoutException) {
            return AuthResult.TIMEOUT
        }
        if (response.status.value in 200..299) {
            // TODO: Добавить логику выхода
            return AuthResult.SUCCESS
        }
        if (response.status.value in 400..499) {
            return try {
                val errors: List<ServerErrorDTO> = response.body()
                return errors.firstOrNull()?.toAuthResult() ?: AuthResult.FAILED
            } catch (e: NoTransformationFoundException) {
                AuthResult.SERIALIZATION_ERROR
            }
        }
        return AuthResult.FAILED
    }
}

