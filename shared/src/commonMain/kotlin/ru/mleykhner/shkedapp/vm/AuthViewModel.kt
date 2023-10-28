package ru.mleykhner.shkedapp.vm

import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.remote.AuthService

@Suppress("RegExpRedundantEscape", "RegExpDuplicateCharacterInClass")
class AuthViewModel: ViewModel(), KoinComponent {

    private val authService: AuthService by inject()
    private val emailAddressRegex: Regex
        get() = Regex(
            """[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""
        )

    val email: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val password: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val fullName: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val group: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val hasError: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val errorReason: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val page: CMutableStateFlow<Page> = MutableStateFlow(Page.EMAIL).cMutableStateFlow()

    val isButtonEnabled: CStateFlow<Boolean> =  MutableStateFlow(email.value.matches(emailAddressRegex)).asStateFlow().cStateFlow()
//        flow {
//        email.value.matches(emailAddressRegex)
//    }

    fun checkEmail() {

    }

    enum class Page {
        EMAIL, PASSWORD_AUTH,
        PASSKEY_AUTH, REGISTRATION_NAME,
        REGISTRATION_GROUP, FINISH
    }

    sealed interface Action {
        object LoginSuccess : Action
    }
}