package ru.mleykhner.shkedapp.data.remote

import de.nycode.bcrypt.hash
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.mp.KoinPlatform
import ru.mleykhner.shkedapp.data.remote.models.ServerErrorDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthDTO
import ru.mleykhner.shkedapp.data.remote.models.auth.SignUpDTO
import ru.mleykhner.shkedapp.data.remote.models.toAuthResult

class AuthService {
    private val client: HttpClient = KoinPlatform.getKoin().get()
    suspend fun signIn(email: String, password: String): List<AuthResult> {
        val response = client.get(HttpRoutes.AUTH_SIGN_IN) {
            url {
                parameters.append("email", email)
                parameters.append("passHash", hash(password).toString())
            }
        }
        if (response.status.value in 200..299) {
            return try {
                val result: AuthDTO = response.body()
                // TODO: Добавить логику авторизации
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
        val response = client.post(HttpRoutes.AUTH_SIGN_UP) {
            setBody(dto)
        }
        if (response.status.value in 200..299) {
            return try {
                val result: AuthDTO = response.body()
                // TODO: Добавить логику регистрации
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
        val response = client.get(HttpRoutes.AUTH_SIGN_IN) {
            url {
                //TODO: Получить Refresh токен
                parameters.append("refreshToken", "")
            }
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
        val response = client.delete(HttpRoutes.AUTH_LOGOUT) {
            url {
                //TODO: Получить Refresh токен
                parameters.append("refreshToken","")
            }
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

    suspend fun logoutFromAll(): AuthResult {
        val response = client.delete(HttpRoutes.AUTH_LOGOUT_FROM_ALL)
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
    FAILED
}

