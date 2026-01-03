package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.model.TimerBase.Running
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class StartTimerUseCase @Inject constructor(private val repository: SchedulerRepository) {

    suspend operator fun invoke(id: UUID) = withContext(Dispatchers.IO) {
        val timer = repository.timerOrNull(id) as? TimerBase.Initial ?: return@withContext

        val running = timer.toRunning(System.currentTimeMillis())
        repository.changeTimer(running)

    }
}

fun TimerBase.Initial.toRunning(startTime: Long): Running {
    return Running(
        id = id,
        createdAt = createdAt,
        expectedDuration = expectedDuration,
        task = task,
        startTime = startTime
    )
}


