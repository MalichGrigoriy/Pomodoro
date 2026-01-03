package com.timemanager.pomodorofocus.ui.timer

import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType

//todo check which thread calculate time to chars on view model and view
//todo remove minutes seconds milliseconds

data class TimerStateHolder(
    val minutes: Int,
    val seconds: Int,
    val milliseconds: Int,
    val millisecondsFull: Int,
    val isNegativeColor: Boolean,
    val isNegativeMark: Boolean,
    val action: TimerAction,
    val selectedTimerType: TimerType,
)
