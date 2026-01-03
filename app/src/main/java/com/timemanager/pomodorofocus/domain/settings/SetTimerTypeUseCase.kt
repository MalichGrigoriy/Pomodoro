package com.timemanager.pomodorofocus.domain.settings

import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetTimerTypeUseCase @Inject constructor(private val repository: SettingsRepository) {

    suspend operator fun invoke(timerType: TimerType) = withContext(Dispatchers.IO) {
        repository.setTimerType(timerType)
    }
}
