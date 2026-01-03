package com.timemanager.pomodorofocus

import android.app.Application
import android.os.Looper
import android.os.StrictMode
import androidx.core.os.HandlerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PomodoroFocusApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG && BuildConfig.IS_PERFORMANCE_TEST_ENABLED) {
            val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
            mainThreadHandler.postDelayed({ enableStrictMode() }, 500)
        }
    }

    fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
