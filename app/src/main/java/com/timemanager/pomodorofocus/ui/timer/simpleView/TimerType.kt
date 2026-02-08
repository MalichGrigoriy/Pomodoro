package com.timemanager.pomodorofocus.ui.timer.simpleView

private const val SECOND_MS = 1000L
private const val HUNDREDTH_SECOND_MS = 10L

enum class TimerType(val delay: Long, val storageKey: String) {
    String(SECOND_MS, "String"),
    StringLong(HUNDREDTH_SECOND_MS, "StringLong"),
    Dual(SECOND_MS, "Dual"),
    Triple(HUNDREDTH_SECOND_MS, "Triple"),
    LedMatrix(SECOND_MS, "LedMatrix"),
    Empty(SECOND_MS, "Empty");

    companion object {
        fun fromStorageKey(key: String?): TimerType {
            return entries.find { it.storageKey == key } ?: String
        }
    }
}
