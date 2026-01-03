package com.timemanager.pomodorofocus.data.localsource.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timemanager.pomodorofocus.domain.model.TimerBase
import java.util.UUID

@Entity(tableName = "timers")
class TimerEntity(
    @PrimaryKey val id: UUID,
    val createdAt: Long,
    val startTime: Long? = null,
    val expectedDuration: Long,
    val actualEndTime: Long? = null,
    @Embedded(prefix = "task-") val task: TaskEntity? = null,
)

fun TimerEntity.toTimerBase(): TimerBase {
    return when {
        startTime == null && actualEndTime == null -> TimerBase.Initial(
            id,
            createdAt,
            expectedDuration,
            task?.toDomain()
        )

        startTime != null && actualEndTime == null -> TimerBase.Running(
            id,
            createdAt,
            expectedDuration,
            task?.toDomain(),
            startTime
        )

        startTime != null && actualEndTime != null -> TimerBase.Completed(
            id,
            createdAt,
            expectedDuration,
            task?.toDomain(),
            startTime,
            actualEndTime
        )

        else -> {
            throw IllegalStateException("Unknown state")
        }
    }
}

fun TimerBase.toEntity() = TimerEntity(
    id = id,
    createdAt = createdAt,
    expectedDuration = expectedDuration,
    task = task?.toEntity(),
    startTime = (this as? TimerBase.Running)?.startTime
        ?: (this as? TimerBase.Completed)?.startTime,
    actualEndTime = (this as? TimerBase.Completed)?.actualEndTime,
)
