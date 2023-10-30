package ru.mleykhner.shkedapp.vm

import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val phoneNumberRegex: Regex
        get() = Regex(
            """\+?\d{1,4}?[-.\s]?\(?\d{1,3}?\)?[-.\s]?\d{1,4}[-.\s]?\d{1,4}[-.\s]?\d{1,9}"""
        )

    val emailOrPhone: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val password: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val fullName: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val group: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val hasError: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val errorReason: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()

    private val _isLoading: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val isLoading: CStateFlow<Boolean> = _isLoading.cStateFlow()

    val isNextButtonEmailOrPhoneAvailable: CStateFlow<Boolean> =
        combine(isLoading, emailOrPhone) { isLoading, emailOrPhone ->
            isLoading.not() && (emailOrPhone.matches(emailAddressRegex) || emailOrPhone.matches(phoneNumberRegex))
        }.stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = false).cStateFlow()

    fun checkEmailOrPhone(): String {
        return "password"
    }

    sealed interface Action {
        object LoginSuccess : Action
    }
}