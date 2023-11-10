package ru.mleykhner.shkedapp.vm

import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.remote.AuthService
import ru.mleykhner.shkedapp.data.remote.models.auth.AuthResult

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
    private val _actions = Channel<Action>()
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

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

    fun updatePassword(value: String) {
        hasError.value = false
        password.value = value
    }

    fun signInWithPassword() {
        Napier.v("Sign In button pressed")
        _isLoading.value = true
        viewModelScope.launch {
            val result = authService.signIn(emailOrPhone.value, password.value)
            _isLoading.value = false
            if (result.contains(AuthResult.SUCCESS)) {
                _actions.send(Action.LoginSuccess)
            } else {
                hasError.value = true
            }
        }
    }

    sealed interface Action {
        data object LoginSuccess : Action
    }
}