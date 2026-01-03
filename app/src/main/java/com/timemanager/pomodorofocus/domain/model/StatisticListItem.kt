package com.timemanager.pomodorofocus.domain.model

import java.util.UUID

sealed class StatisticListItem {
    data class Header(val date: String) : StatisticListItem()
    data class StatisticFinishedItem(
        val id: UUID,
        val createdAt: Long,
        val startTime: Long,
        val percent: Int,
        val duration: Long,
        val taskTitle: String,
        val taskDescription: String,
    ) : StatisticListItem()
}
