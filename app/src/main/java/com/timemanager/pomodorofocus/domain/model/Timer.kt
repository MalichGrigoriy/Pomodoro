package com.timemanager.pomodorofocus.domain.model

import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import java.util.UUID

sealed class TimerBase(
    open val id: UUID,
    open val createdAt: Long,
    open val expectedDuration: Long = DEFAULT_TIMER_DURATION,
    open val task: Task? = null
) {
    abstract fun change(
        task: Task? = this.task,
        expectedDuration: Long = this.expectedDuration
    ): TimerBase

    data class Empty(
        override val id: UUID = UUID.randomUUID(),
        override val createdAt: Long = System.currentTimeMillis(),
        override val expectedDuration: Long = 0,
        override val task: Task? = null
    ): TimerBase(id, createdAt, expectedDuration, task) {

        override fun change(task: Task?, expectedDuration: Long): TimerBase {
            return this.copy(
                task = task,
                expectedDuration = expectedDuration
            )
        }
    }

    data class Initial(
        override val id: UUID,
        override val createdAt: Long,
        override val expectedDuration: Long = DEFAULT_TIMER_DURATION,
        override val task: Task? = null
    ) : TimerBase(id, createdAt, expectedDuration, task) {

        override fun change(task: Task?, expectedDuration: Long): TimerBase {
            return this.copy(
                task = task,
                expectedDuration = expectedDuration
            )
        }
    }

    data class Running(
        override val id: UUID,
        override val createdAt: Long,
        override val expectedDuration: Long = DEFAULT_TIMER_DURATION,
        override val task: Task? = null,
        val startTime: Long
    ) : TimerBase(id, createdAt, expectedDuration, task) {

        override fun change(task: Task?, expectedDuration: Long): TimerBase {
            return this.copy(
                task = task,
                expectedDuration = expectedDuration
            )
        }
    }

    data class Completed(
        override val id: UUID,
        override val createdAt: Long,
        override val expectedDuration: Long = DEFAULT_TIMER_DURATION,
        override val task: Task? = null,
        val startTime: Long,
        val actualEndTime: Long
    ) : TimerBase(id, createdAt, expectedDuration, task) {

        override fun change(task: Task?, expectedDuration: Long): TimerBase {
            return this.copy(
                task = task,
                expectedDuration = expectedDuration
            )
        }
    }
}
