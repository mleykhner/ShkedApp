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
class SignInViewModel: ViewModel(), KoinComponent {

    private val authService: AuthService by inject()
    private val emailAddressRegex: Regex
        get() = Regex(
            """[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""
        )

    val email: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val password: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val areCredentialsWrong: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()

    private val _isLoading: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val isLoading: CStateFlow<Boolean> = _isLoading.cStateFlow()

    val isButtonEnabled: CStateFlow<Boolean> =
        combine(isLoading, email, password) { isLoading, email, password ->

            isLoading.not() && email.matches(emailAddressRegex) && password.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false).cStateFlow()

    fun onLoginPressed() {
        Napier.v("Login button pressed")
        _isLoading.value = true
        viewModelScope.launch {
            val result = authService.signIn(email.value, password.value)
            _isLoading.value = false
            if (result.contains(AuthResult.SUCCESS)) {
                _actions.send(Action.LoginSuccess)
            } else {
                areCredentialsWrong.value = true
            }
        }
    }

    private val _actions = Channel<Action>()
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    sealed interface Action {
        object LoginSuccess : Action
    }
}