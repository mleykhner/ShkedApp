package ru.mleykhner.shkedapp.vm

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _areCredentialsWrong: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val areCredentialsWrong: StateFlow<Boolean> = _areCredentialsWrong

    val isButtonEnabled: StateFlow<Boolean> =
        combine(isLoading, email, password) { isLoading, email, password ->
            isLoading.not() && email.isNotBlank() && password.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
    val actions: Flow<Action> get() = _actions.receiveAsFlow()

    sealed interface Action {
        object LoginSuccess : Action
    }
}