package com.timemanager.pomodorofocus.domain.settings

import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType

const val DEFAULT_TIMER_DURATION = 1500_000L // 25 minutes
const val AUTO_STOP_TIMER_DURATION = -300_000L // 5 min
const val AUTO_STOP_ENABLE: Boolean = true
const val TIMER_NEGATIVE_MARK = false
const val TIMER_NEGATIVE_COLOR = true

//first value is default
val TIMER_TYPE_LIST = listOf(
    TimerType.String,
    TimerType.Dual,
    TimerType.LedMatrix,
    TimerType.Triple,
)

val TIMER_TYPE_DEFAULT = TIMER_TYPE_LIST.first()


