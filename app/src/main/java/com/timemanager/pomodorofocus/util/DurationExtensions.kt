package com.timemanager.pomodorofocus.util

import kotlin.time.Duration

val Duration.onlyMinutes: Long
    get() = inWholeMinutes % 60

val Duration.onlySeconds: Long
    get() = inWholeSeconds % 60

val Duration.onlyTenthsOfSecond: Long
    get() = inWholeMilliseconds % 1000 / 100

val Duration.onlyHundredthsOfSecond: Long
    get() = inWholeMilliseconds % 1000 / 10

val Duration.onlyMillisecondsFraction: Long
    get() = inWholeMilliseconds % 1000
