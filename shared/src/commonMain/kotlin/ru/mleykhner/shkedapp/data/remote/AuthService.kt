package ru.mleykhner.shkedapp.data.remote

import com.liftric.kvault.KVault
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.remote.models.ServerErrorDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.SignUpDTO
import ru.mleykhner.shkedapp.data.remote.models.toAuthResult

class AuthService: KoinComponent {
    private val client: HttpClient by inject()
    private val kvault: KVault by inject()

    suspend fun signIn(email: String, password: String): List<AuthResult> {
        val response = try {
            client.get(HttpRoutes.AUTH_SIGN_IN) {
                url {
                    parameters.append("email", email)
                    parameters.append("password", password)
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            return listOf(AuthResult.TIMEOUT)
        }
        if (response.status.value in 200..299) {
            return try {
                val result: AuthDTO = response.body()
                // TODO: Улучшить логику авторизации
                kvault.set("refreshToken", result.refreshToken)
                kvault.set("accessToken", result.accessToken)
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
                    //Log.e("AuthService", "SignIn error. Code: ")
                    it.toAuthResult()
                }
            } catch (e: NoTransformationFoundException) {
                listOf(AuthResult.SERIALIZATION_ERROR)
            }
        }
        return listOf(AuthResult.FAILED)
    }

    suspend fun signUp(dto: SignUpDTO): List<AuthResult> {
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
                kvault.set("refreshToken", result.refreshToken)
                kvault.set("accessToken", result.accessToken)

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

    suspend fun refresh(): AuthResult {
        val token = kvault.string("refreshToken") ?: return AuthResult.INVALID_REFRESH_TOKEN
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

    suspend fun logout(): AuthResult {
        val token = kvault.string("refreshToken") ?: return AuthResult.INVALID_REFRESH_TOKEN
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
            // TODO: Улучшить логику выхода
            kvault.deleteObject("refreshToken")
            kvault.deleteObject("accessToken")

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

    suspend fun logoutFromAll(): AuthResult {
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

enum class AuthResult {
    SUCCESS,
    WRONG_CREDENTIALS,
    EMAIL_OCCUPIED,
    INVALID_REFRESH_TOKEN,
    SERIALIZATION_ERROR,
    INVALID_GROUP,
    TIMEOUT,
    FAILED
}

