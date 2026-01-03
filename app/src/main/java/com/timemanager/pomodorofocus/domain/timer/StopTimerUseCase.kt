package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.model.TimerBase.Completed
import com.timemanager.pomodorofocus.domain.settings.AUTO_STOP_TIMER_DURATION
import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class StopTimerUseCase @Inject constructor(private val repository: SchedulerRepository) {

    suspend fun manualStop(id: UUID) = stop(id, false)

    suspend fun autoStop(id: UUID) = stop(id, true)

    private suspend fun stop(id: UUID, isAutoStop: Boolean) = withContext(Dispatchers.IO) {
        val timer = repository.timerOrNull(id) as? TimerBase.Running ?: return@withContext

        val actualEndTime: Long = if (isAutoStop) {
            timer.startTime + DEFAULT_TIMER_DURATION - AUTO_STOP_TIMER_DURATION
        } else {
            System.currentTimeMillis()
        }

        val completed = timer.toCompleted(actualEndTime)
        repository.changeTimer(completed)
    }

}

private fun TimerBase.Running.toCompleted(actualEndTime: Long) = Completed(
    id = id,
    createdAt = createdAt,
    expectedDuration = expectedDuration,
    task = task,
    startTime = startTime,
    actualEndTime = actualEndTime
)

