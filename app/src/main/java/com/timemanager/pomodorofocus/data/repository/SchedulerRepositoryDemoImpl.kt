package com.timemanager.pomodorofocus.data.repository

import androidx.paging.PagingSource
import com.timemanager.pomodorofocus.data.localsource.entity.TimerEntity
import com.timemanager.pomodorofocus.domain.timer.SchedulerRepository
import com.timemanager.pomodorofocus.domain.model.Task
import com.timemanager.pomodorofocus.domain.model.TimerBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulerRepositoryDemoImpl @Inject constructor() :
    SchedulerRepository {

    private var _timerList = MutableStateFlow(listOf<TimerBase>())
    private val timerList: StateFlow<List<TimerBase>> = _timerList

    private val scope = CoroutineScope(Dispatchers.Default)
    private val currentTimer: StateFlow<TimerBase?> = _timerList.map { it.lastOrNull() ?: TimerBase.Empty() }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override suspend fun timerOrNull(id: UUID): TimerBase? {
        return _timerList.value.find { it.id == id }
    }

    override suspend fun createTimer(expectedDuration: Long, task: Task?) {
        _timerList.value += TimerBase.Initial(
            id = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            task = task,
        )
    }

    override  suspend fun changeTimer(timer: TimerBase) {
        _timerList.value = _timerList.value.map {
            if (it.id == timer.id) {
                timer
            } else {
                it
            }
        }
    }

    override suspend fun currentTimerOrNull(): TimerBase? {
        return currentTimer.value
    }

    override fun currentTimerFlow(): Flow<TimerBase?> {
        return currentTimer
    }

    override fun timerListFlow(): Flow<List<TimerBase>> {
        return timerList
    }

    override fun pagedFinishedTimers(): PagingSource<Int, TimerEntity> {
        TODO("Not yet implemented")
    }
}
