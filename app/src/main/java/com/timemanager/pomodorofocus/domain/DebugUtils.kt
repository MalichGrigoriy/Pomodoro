package com.timemanager.pomodorofocus.domain

import android.util.Log
import com.timemanager.pomodorofocus.BuildConfig
import kotlinx.coroutines.currentCoroutineContext

suspend fun debugOnlyDelay(message: String? = null, tag: String = "DELAY_TAG") {
    if (BuildConfig.DEBUG && BuildConfig.IS_PERFORMANCE_TEST_ENABLED && BuildConfig.THREAD_SLEEP_MS != null) {

        val callers = getCallers()

        Log.d(tag, "0 delay start  -> $message")
        Log.d(tag, "1 delay thread -> ${Thread.currentThread().name} ${currentCoroutineContext()}")
        Log.d(tag, "2 delay called -> $callers")
        Thread.sleep(BuildConfig.THREAD_SLEEP_MS)
        Log.d(tag, "4 delay finish -> $message")
        Log.d(tag, "5 delay thread -> ${Thread.currentThread().name} ${currentCoroutineContext()}")
        Log.d(tag, "6 delay called -> $callers")
    }
}

suspend fun logCoroutine(message: String? = null, tag: String = "COROUTINE_TAG") {
    if (BuildConfig.DEBUG) {
        Log.d(tag, "0 message -> $message")
        Log.d(tag, "1 thread  -> ${Thread.currentThread().name}  ${currentCoroutineContext()}")
        Log.d(tag, "2 called  -> ${getCallers()}")
    }
}

fun logThread(message: String? = null, tag: String = "THREAD_TAG") {
    Log.d(tag, "Running on thread: ${Thread.currentThread().name} ")
    message?.let { Log.d(tag, it) }
}

fun getCallers(): String {
    val stackTrace = Throwable().stackTrace

    val callers = stackTrace.first {
        it.toString().contains("com.timemanager.pomodorofocus") &&
                it.toString().contains("DebugUtils").not() //todo fix filter by name
    }.toString()

    return callers.ifEmpty { "Unknown" }
}
