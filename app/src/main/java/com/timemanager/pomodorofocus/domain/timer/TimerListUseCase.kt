package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class TimerListUseCase @Inject constructor(private val repository: SchedulerRepository) {

    operator fun invoke(): Flow<List<TimerBase>> = repository.timerListFlow()
        .flowOn(Dispatchers.IO)
}
