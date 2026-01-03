package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class CurrentTimerUseCase @Inject constructor(private val repository: SchedulerRepository) {
    operator fun invoke(): Flow<TimerBase> =
        repository.currentTimerFlow()
            .onEach { timer ->
                if (timer == null) {
                    repository.createTimer(DEFAULT_TIMER_DURATION)
                }
            }
            .filterNotNull()
            .flowOn(Dispatchers.IO)
}
