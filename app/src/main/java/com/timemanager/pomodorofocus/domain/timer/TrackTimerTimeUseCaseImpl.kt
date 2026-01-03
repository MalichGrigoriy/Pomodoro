package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.GetTimerTypeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class TrackTimerTimeUseCase @Inject constructor(
    private val getCurrentTimer: CurrentTimerUseCase,
    private val getTimerTypeUseCase: GetTimerTypeUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Long> {
        return combine(
            getCurrentTimer(),
            getTimerTypeUseCase()
        ) { a, b -> a to b }
            .transformLatest { pair ->
                val (timer, type) = pair
                when (timer) {
                    is TimerBase.Empty,
                    is TimerBase.Initial -> {
                        emit(timer.expectedDuration)
                        return@transformLatest
                    }

                    is TimerBase.Completed -> {
                        emit(timer.expectedDuration - (timer.actualEndTime - timer.startTime))
                        return@transformLatest
                    }

                    is TimerBase.Running -> {
                        runTimer(timer, type.delay)
                    }
                }
            }
    }
}

private suspend fun FlowCollector<Long>.runTimer(timer: TimerBase.Running, delayTime: Long) {
    while (true) {
        val currentTime = System.currentTimeMillis()
        val emitValue = timer.expectedDuration + timer.startTime - currentTime

        emit(emitValue)
        val nextDelay = delayTime - (currentTime % delayTime)
        delay(nextDelay)
    }
}
