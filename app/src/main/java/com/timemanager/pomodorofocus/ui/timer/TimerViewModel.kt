package com.timemanager.pomodorofocus.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.AUTO_STOP_ENABLE
import com.timemanager.pomodorofocus.domain.settings.AUTO_STOP_TIMER_DURATION
import com.timemanager.pomodorofocus.domain.settings.GetTimerTypeUseCase
import com.timemanager.pomodorofocus.domain.settings.TIMER_NEGATIVE_COLOR
import com.timemanager.pomodorofocus.domain.settings.TIMER_NEGATIVE_MARK
import com.timemanager.pomodorofocus.domain.timer.ChangeTaskUseCase
import com.timemanager.pomodorofocus.domain.timer.CurrentTimerUseCase
import com.timemanager.pomodorofocus.domain.timer.RestartTimerUseCase
import com.timemanager.pomodorofocus.domain.timer.StartTimerUseCase
import com.timemanager.pomodorofocus.domain.timer.StopTimerUseCase
import com.timemanager.pomodorofocus.domain.timer.TrackTimerTimeUseCase
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import com.timemanager.pomodorofocus.util.onlyHundredthsOfSecond
import com.timemanager.pomodorofocus.util.onlySeconds
import com.timemanager.pomodorofocus.util.waitForNext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val startTimer: StartTimerUseCase,
    private val restartTimerUseCase: RestartTimerUseCase,
    private val stopTimer: StopTimerUseCase,
    private val changeTask: ChangeTaskUseCase,
    private val getCurrentTimer: CurrentTimerUseCase,
    private val getTimerTypeUseCase: GetTimerTypeUseCase,
    private val trackTimerTimeUseCase: TrackTimerTimeUseCase,
) : ViewModel() {

    private val _timerMillis: StateFlow<Long> = trackTimerTimeUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    val millisecondsFull: StateFlow<Int> = _timerMillis.map { it.absoluteValue.toInt() }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val minutes: StateFlow<Int> =
        _timerMillis.map { (it.absoluteValue.milliseconds.inWholeMinutes).toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val seconds: StateFlow<Int> =
        _timerMillis.map { (it.absoluteValue.milliseconds.onlySeconds).toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val milliseconds: StateFlow<Int> =
        _timerMillis.map { (it.absoluteValue.milliseconds.onlyHundredthsOfSecond).toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val isNegativeColor: StateFlow<Boolean> =
        _timerMillis.map { it < 0 && TIMER_NEGATIVE_COLOR }
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val isNegativeMark: StateFlow<Boolean> = _timerMillis.map { it < 0 && TIMER_NEGATIVE_MARK }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _currentTimer: StateFlow<TimerUI> = getCurrentTimer()
        .map {
            it.toTimerUI()
        }.stateIn(viewModelScope, SharingStarted.Lazily, TimerBase.Empty().toTimerUI())


    private val autostop = if (AUTO_STOP_ENABLE) {
        _currentTimer.filter { it.state is TimerState.Running }
            .waitForNext(_timerMillis.filter { it <= AUTO_STOP_TIMER_DURATION })
            .onEach { (runningTimer, _) -> stopTimer.autoStop(runningTimer.id) }
            .launchIn(viewModelScope)
    } else {
        null
    }

    private val _task = MutableStateFlow<String>(_currentTimer.value.task)
    val task: StateFlow<String> = merge(_task, _currentTimer.map { it.task })
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val action: StateFlow<TimerAction> = _currentTimer
        .map { it.state.action }
        .stateIn(viewModelScope, SharingStarted.Lazily, TimerAction.EMPTY)

    val selectedTimerType: StateFlow<TimerType> = getTimerTypeUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, TimerType.Empty)

    fun changeTask(text: String) {
        _task.value = text
    }

    fun saveTask() {
        viewModelScope.launch {
            changeTask(_currentTimer.value.id, _task.value)
        }
    }

    fun onTimerClick() {
        when (_currentTimer.value.state.action) {
            TimerAction.START -> startTimer()
            TimerAction.STOP -> stopTimer()
            TimerAction.RESTART -> restartTimer()
            TimerAction.EMPTY -> {
                // Do nothing
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            startTimer(_currentTimer.value.id)
        }
    }

    private fun stopTimer() {
        viewModelScope.launch {
            stopTimer.manualStop(_currentTimer.value.id)
        }
    }

    private fun restartTimer() {
        viewModelScope.launch {
            restartTimerUseCase()
        }
    }
}
