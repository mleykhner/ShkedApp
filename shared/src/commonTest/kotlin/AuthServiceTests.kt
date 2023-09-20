
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import ru.mleykhner.shkedapp.data.remote.AuthServiceImpl
import ru.mleykhner.shkedapp.data.remote.TokensService
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult
import ru.mleykhner.shkedapp.data.remote.models.auth.SignUpDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthServiceTests: KoinTest {
    @Test
    fun `Sign In with correct credentials`() {
        val mockEngine = MockEngine { request ->
            val parameters = request.url.parameters
            val requestEmail = parameters["email"]
            val requestPassword = parameters["password"]
            if (requestEmail == "test@test.ru" && requestPassword == "123456") {
                respond(
                    content = ByteReadChannel("""{"accessToken":"accessToken","refreshToken":"refreshToken","uuid":"uuid"}"""),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else {
                respond(
                    content = ByteReadChannel("""[{"errorCode":2,"errorMessage":"Wrong credentials"}]"""),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
        startKoin {
            modules(
                module {
                    single<TokensService> {
                        object : TokensService {
                            override fun updateTokens(tokens: BearerTokens) {}
                            override fun getTokens(): BearerTokens? {
                                return BearerTokens("123456", "abcdef")
                            }
                            override fun deleteTokens() {}
                        }
                    }
                    single {
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                json()
                            }
                        }
                    }
                }
            )
        }

        val authService = AuthServiceImpl()

        runBlocking {
            val result = authService.signIn("test@test.ru", "123456")
            assertEquals(expected = listOf(AuthResult.SUCCESS), actual = result)
        }

        stopKoin()
    }

    @Test
    fun `Sign In with wrong credentials`() {
        val mockEngine = MockEngine { request ->
            val parameters = request.url.parameters
            val requestEmail = parameters["email"]
            val requestPassword = parameters["password"]
            if (requestEmail == "test@test.ru" && requestPassword == "123456") {
                respond(
                    content = ByteReadChannel("""{"accessToken":"accessToken","refreshToken":"refreshToken","uuid":"uuid"}"""),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else {
                respond(
                    content = ByteReadChannel("""[{"errorCode":2,"errorMessage":"Wrong credentials"}]"""),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
        startKoin {
            modules(
                module {
                    single<TokensService> {
                        object : TokensService {
                            override fun updateTokens(tokens: BearerTokens) {

                            }
                            override fun getTokens(): BearerTokens? {
                                return BearerTokens("123456", "abcdef")
                            }
                            override fun deleteTokens() {}
                        }
                    }
                    single {
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                json()
                            }
                        }
                    }
                }
            )
        }

        val authService = AuthServiceImpl()

        runBlocking {
            val result = authService.signIn("wrong", "wrong")
            assertTrue(result.contains(AuthResult.WRONG_CREDENTIALS))
        }

        stopKoin()
    }

    @Test
    fun `Sign Up with correct credentials`() {
        val mockEngine = MockEngine { request ->
            val body: SignUpDTO = Json.decodeFromString(request.body.)
            if (
                body.email == "test@test.ru" &&
                body.password == "123456" &&
                body.fullName == "name" &&
                body.group == "group"
                ) {
                    respond(
                    content = ByteReadChannel("""{"accessToken":"accessToken","refreshToken":"refreshToken","uuid":"uuid"}"""),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
            } else {
                respond(
                    content = ByteReadChannel("""[{"errorCode":2,"errorMessage":"Wrong credentials"}]"""),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
        startKoin {
            modules(
                module {
                    single<TokensService> {
                        object : TokensService {
                            override fun updateTokens(tokens: BearerTokens) {
                                assertEquals(tokens, BearerTokens("accessToken", "refreshToken"))
                            }
                            override fun getTokens(): BearerTokens? {
                                return BearerTokens("accessToken", "refreshToken")
                            }
                            override fun deleteTokens() {}
                        }
                    }
                    single {
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                json()
                            }
                        }
                    }
                }
            )
        }
        val dto = SignUpDTO(
            fullName = "name",
            email = "test@test.ru",
            password = "123456",
            group = "group"
        )
        val authService = AuthServiceImpl()
        runBlocking {
            val result = authService.signUp(dto)
            assertTrue(result.contains(AuthResult.SUCCESS))
        }
    }
}