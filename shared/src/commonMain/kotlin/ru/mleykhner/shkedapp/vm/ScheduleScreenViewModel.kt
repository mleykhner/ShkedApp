package ru.mleykhner.shkedapp.vm

import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.ScheduleRefreshResult
import ru.mleykhner.shkedapp.data.ScheduleService

class ScheduleScreenViewModel: ViewModel(), KoinComponent {

    private val scheduleService: ScheduleService by inject()

    val initialDate: LocalDate get() = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private var _selectedDate: LocalDate = initialDate
    var selectedDate: LocalDate
        get() = _selectedDate
        set(value) {
            _selectedDate = value
            viewModelScope.launch {
                _actions.emit(Action.DateChanged)
            }
        }

//    var _visibleMonth: MutableStateFlow<String> =

    private val _isLoading = MutableStateFlow(false).cMutableStateFlow()
    val isLoading = _isLoading.cStateFlow()

    private val _actions = MutableSharedFlow<Action>()
    val actions: CFlow<Action> get() = _actions.asSharedFlow().cFlow()

    fun updateSchedule(group: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _actions.emit(
                when (scheduleService.refresh(group)) {
                    ScheduleRefreshResult.REFRESHED -> Action.Refreshed
                    ScheduleRefreshResult.HAS_CHANGES -> Action.HasChanges
                    ScheduleRefreshResult.OFFLINE -> Action.NoConnection
                    ScheduleRefreshResult.FAILED -> Action.Failed
                }
            )
            _isLoading.value = false
        }
    }

    fun backToToday() {
        selectedDate = initialDate
    }

    sealed interface Action {
        data object DateChanged: Action
        data object NoConnection: Action
        data object Failed: Action
        data object Refreshed: Action
        data object HasChanges: Action
    }
}