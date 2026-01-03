package com.timemanager.pomodorofocus.ui.timer.simpleView

private const val SECOND_MS = 1000L
private const val HUNDREDTH_SECOND_MS = 10L

enum class TimerType(val delay: Long) {
    String(SECOND_MS),
    StringLong(HUNDREDTH_SECOND_MS),
    Dual(SECOND_MS),
    Triple(HUNDREDTH_SECOND_MS),
    LedMatrix(SECOND_MS),
    Empty(SECOND_MS)
}

fun timerTypeByName(type: String?): TimerType {
    return when (type) {
        TimerType.Dual.name -> TimerType.Dual
        TimerType.Triple.name -> TimerType.Triple
        TimerType.String.name -> TimerType.String
        TimerType.StringLong.name -> TimerType.StringLong
        TimerType.LedMatrix.name -> TimerType.LedMatrix
        else -> TimerType.String
    }
}
