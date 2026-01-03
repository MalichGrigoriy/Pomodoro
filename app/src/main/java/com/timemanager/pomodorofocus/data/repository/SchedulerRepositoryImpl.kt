package com.timemanager.pomodorofocus.data.repository

import androidx.paging.PagingSource
import com.timemanager.pomodorofocus.data.localsource.TimerDao
import com.timemanager.pomodorofocus.data.localsource.entity.TimerEntity
import com.timemanager.pomodorofocus.data.localsource.entity.toEntity
import com.timemanager.pomodorofocus.data.localsource.entity.toTimerBase
import com.timemanager.pomodorofocus.domain.model.Task
import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.timer.SchedulerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class SchedulerRepositoryImpl @Inject constructor(
    private val localSource: TimerDao,
) : SchedulerRepository {

    private val timerListFlow: Flow<List<TimerBase>> = localSource.getTimersFlow()
        .map { list -> list.map { it.toTimerBase() } }

    override suspend fun currentTimerOrNull(): TimerBase? = timerListFlow.firstOrNull()?.maxByOrNull { it.createdAt }

    override fun currentTimerFlow(): Flow<TimerBase?> =
        timerListFlow.map { it.lastOrNull() }

    override suspend fun timerOrNull(id: UUID): TimerBase? = localSource.getTimerById(id)?.toTimerBase()

    override suspend fun createTimer(expectedDuration: Long, task: Task?) {
            val newTimer = TimerEntity(
                id = UUID.randomUUID(),
                createdAt = System.currentTimeMillis(),
                task = task?.toEntity(),
                expectedDuration = expectedDuration
            )
            localSource.insertTimer(newTimer)
        }

    override suspend fun changeTimer(timer: TimerBase) = localSource.updateTimer(timer.toEntity())

    override fun timerListFlow(): Flow<List<TimerBase>> = timerListFlow

    override fun pagedFinishedTimers(): PagingSource<Int, TimerEntity> {
        return localSource.getPagedTimer()
    }
}
