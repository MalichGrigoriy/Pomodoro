package com.timemanager.pomodorofocus.util

import java.util.Locale

fun String.capitalizeString(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
