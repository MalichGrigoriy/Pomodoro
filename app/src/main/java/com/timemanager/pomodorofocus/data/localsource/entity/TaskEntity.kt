package com.timemanager.pomodorofocus.data.localsource.entity

import com.timemanager.pomodorofocus.domain.model.Task

class TaskEntity(
    val name: String
)

fun TaskEntity.toDomain() = Task(
    name = name
)

fun Task.toEntity() = TaskEntity(
    name = name
)
