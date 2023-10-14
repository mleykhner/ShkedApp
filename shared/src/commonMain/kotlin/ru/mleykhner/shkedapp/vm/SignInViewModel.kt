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

class SignInViewModel: ViewModel(), KoinComponent {

    private val authService: AuthService by inject()

    private val _email: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val email: CStateFlow<String> = _email.cStateFlow()

    private val _password: CMutableStateFlow<String> = MutableStateFlow("").cMutableStateFlow()
    val password: CStateFlow<String> = _password.cStateFlow()

    private val _isLoading: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val isLoading: CStateFlow<Boolean> = _isLoading.cStateFlow()

    private val _areCredentialsWrong: CMutableStateFlow<Boolean> = MutableStateFlow(false).cMutableStateFlow()
    val areCredentialsWrong: CStateFlow<Boolean> = _areCredentialsWrong.cStateFlow()

    val isButtonEnabled: CStateFlow<Boolean> =
        combine(isLoading, email, password) { isLoading, email, password ->
            isLoading.not() && email.isNotBlank() && password.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false).cStateFlow()

    fun emailUpdate(new: String) {
        _areCredentialsWrong.value = false
        _email.value = new
    }

    fun passwordUpdate(new: String) {
        _areCredentialsWrong.value = false
        _password.value = new
    }

    fun onLoginPressed() {
        Napier.v("Login button pressed")
        _isLoading.value = true
        viewModelScope.launch {
            val result = authService.signIn(_email.value, _password.value)
            _isLoading.value = false
            if (result.contains(AuthResult.SUCCESS)) {
                _actions.send(Action.LoginSuccess)
            } else {
                _areCredentialsWrong.value = true
            }
        }
    }

    private val _actions = Channel<Action>()
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    sealed interface Action {
        object LoginSuccess : Action
    }
}