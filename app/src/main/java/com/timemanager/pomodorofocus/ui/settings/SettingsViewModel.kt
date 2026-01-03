package com.timemanager.pomodorofocus.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timemanager.pomodorofocus.domain.settings.GetTimerTypeUseCase
import com.timemanager.pomodorofocus.domain.settings.SetTimerTypeUseCase
import com.timemanager.pomodorofocus.domain.settings.TIMER_TYPE_LIST
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val getTimerType: GetTimerTypeUseCase,
    private val setTimerType: SetTimerTypeUseCase,
) : ViewModel() {

    private val selectedType: StateFlow<TimerType> = getTimerType()
        .stateIn(viewModelScope, SharingStarted.Lazily, TimerType.Empty)

    val typeListWithSelected: StateFlow<Pair<List<TimerType>, TimerType>> = getTimerType()
        .take(1)
        .map {
            Pair(TIMER_TYPE_LIST, it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Pair(listOf(TimerType.Empty), TimerType.Empty)
        )

    fun setTimerType(timerType: TimerType, isProgrammaticChanges: Boolean) {
        if (!isProgrammaticChanges && timerType != selectedType.value) {
            viewModelScope.launch {
                setTimerType(timerType)
            }
        }
    }
}

