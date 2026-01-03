package com.timemanager.pomodorofocus.domain.statistic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.timemanager.pomodorofocus.data.localsource.entity.toTimerBase
import com.timemanager.pomodorofocus.domain.model.StatisticListItem
import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.timer.SchedulerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimerStatisticUseCase @Inject constructor(private val repository: SchedulerRepository) {

    operator fun invoke(): Flow<PagingData<StatisticListItem>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false)
        ) {
            repository.pagedFinishedTimers()
        }.flow
            .map { pagingData ->
                pagingData
                    .map { it.toTimerBase() }
                    .filter { it is TimerBase.Completed }
                    .map { (it as TimerBase.Completed).toStatisticFinishedItem() }
                    .insertSeparators { before, after ->
                        if (before == null || shouldInsertHeader(before, after)) {
                            after?.let {
                                StatisticListItem.Header(
                                    formatDateByCalendar(it.startTime)
                                )
                            }
                        } else {
                            null
                        }
                    }
            }
            .flowOn(Dispatchers.IO)
}

private fun shouldInsertHeader(
    before: StatisticListItem.StatisticFinishedItem?,
    after: StatisticListItem.StatisticFinishedItem?
): Boolean {
    return before?.startTime?.let { getYearMonthDay(it) } !=
            after?.startTime?.let { getYearMonthDay(it) }
}

private fun getYearMonthDay(timestamp: Long): Triple<Int, Int, Int> {
    val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
    return Triple(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

private fun formatDateByCalendar(timestamp: Long): String {
    val fullDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val shortDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    return when {
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> {
            when (now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR)) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> shortDateFormat.format(date.time)
            }
        }

        else -> fullDateFormat.format(date.time)
    }
}

fun TimerBase.Completed.toStatisticFinishedItem(): StatisticListItem.StatisticFinishedItem {
    val duration = actualEndTime - startTime
    val durationPercentReal =
        ((duration.toFloat() / expectedDuration) * 100).toInt().coerceIn(0, 100)
    val durationPercentSown = when (durationPercentReal) {
        in 0..98 -> durationPercentReal
        else -> 100
    }

    val description = if (task?.name?.isNotBlank() == true) {
        task.name
    } else {
        "No description"
    }

    return StatisticListItem.StatisticFinishedItem(
        id = id,
        createdAt = createdAt,
        startTime = startTime,
        duration = actualEndTime - startTime,
        percent = durationPercentSown,
        taskTitle = "Timer Project",
        taskDescription = description,
    )
}
