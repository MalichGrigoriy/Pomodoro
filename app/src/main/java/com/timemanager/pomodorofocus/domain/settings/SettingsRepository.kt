package com.timemanager.pomodorofocus.domain.settings

import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getTimerType(): Flow<TimerType>
    suspend fun setTimerType(timerType: TimerType)
}
