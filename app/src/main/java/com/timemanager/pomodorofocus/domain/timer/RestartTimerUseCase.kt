package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestartTimerUseCase @Inject constructor(private val repository: SchedulerRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val timer: TimerBase.Completed =
            repository.currentTimerOrNull() as? TimerBase.Completed ?: return@withContext

        repository.createTimer(
            expectedDuration = DEFAULT_TIMER_DURATION,
            task = timer.task
        )
    }
}
