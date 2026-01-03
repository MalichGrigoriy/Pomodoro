package com.timemanager.pomodorofocus.domain.settings

import com.timemanager.pomodorofocus.domain.debugOnlyDelay
import com.timemanager.pomodorofocus.domain.logCoroutine
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetTimerTypeUseCase @Inject constructor(private val repository: SettingsRepository) {

    operator fun invoke(): Flow<TimerType> {
        return repository.getTimerType()
            .distinctUntilChanged()
            .map {
                debugOnlyDelay()
                logCoroutine("GetTimerTypeUseCase selectedTimerType $it")
                if (it in TIMER_TYPE_LIST) it
                else TIMER_TYPE_DEFAULT
            }
            .flowOn(Dispatchers.IO)
    }
}
