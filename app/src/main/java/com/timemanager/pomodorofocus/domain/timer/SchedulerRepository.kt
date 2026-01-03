package com.timemanager.pomodorofocus.domain.timer

import androidx.paging.PagingSource
import com.timemanager.pomodorofocus.data.localsource.entity.TimerEntity
import com.timemanager.pomodorofocus.domain.model.Task
import com.timemanager.pomodorofocus.domain.model.TimerBase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SchedulerRepository {

    fun currentTimerFlow(): Flow<TimerBase?>
    fun timerListFlow(): Flow<List<TimerBase>>
    suspend fun currentTimerOrNull(): TimerBase?

    suspend fun timerOrNull(id: UUID): TimerBase?
    suspend fun createTimer(expectedDuration: Long, task: Task? = null)
    suspend fun changeTimer(timer: TimerBase)

    fun pagedFinishedTimers(): PagingSource<Int, TimerEntity>
}
