package com.timemanager.pomodorofocus.ui.timer

import com.timemanager.pomodorofocus.domain.model.TimerBase
import java.util.UUID

data class TimerUI(
    val id: UUID,
    val duration: Long,
    val state: TimerState,
    val task: String = ""
)

enum class TimerAction {
    START, STOP, RESTART, EMPTY
}

fun TimerBase.toTimerUI(): TimerUI {
    return when (this) {
        is TimerBase.Initial -> TimerUI(
            id,
            expectedDuration,
            TimerState.Initial(),
            task?.name ?: ""
        )

        is TimerBase.Running -> TimerUI(
            id,
            expectedDuration,
            TimerState.Running(startTime),
            task?.name ?: ""
        )

        is TimerBase.Completed -> TimerUI(
            id,
            expectedDuration,
            TimerState.Completed(startTime, actualEndTime),
            task?.name ?: ""
        )

        is TimerBase.Empty -> TimerUI(
            id,
            expectedDuration,
            TimerState.Empty(),
            task?.name ?: ""
        )
    }
}

sealed class TimerState(val action: TimerAction) {
    class Empty : TimerState(action = TimerAction.EMPTY)
    class Initial : TimerState(action = TimerAction.START)
    class Running(val startTime: Long) : TimerState(TimerAction.STOP)
    class Completed(val startTime: Long, val actualEndTime: Long) : TimerState(TimerAction.RESTART)

    override fun toString(): String {
        return when (this) {
            is Initial -> "New(action=$action)"
            is Running -> "InProgress(startTime=$startTime, action=$action)"
            is Completed -> "Finished(startTime=$startTime, actualEndTime=$actualEndTime, action=$action)"
            is Empty -> "Empty(action=$action)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimerState
        return action == other.action
    }

    override fun hashCode(): Int {
        return action.hashCode()
    }
}
