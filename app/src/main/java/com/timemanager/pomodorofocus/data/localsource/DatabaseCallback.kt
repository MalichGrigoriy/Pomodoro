package com.timemanager.pomodorofocus.data.localsource

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.timemanager.pomodorofocus.data.localsource.entity.toEntity
import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val timerDaoProvider: Provider<TimerDao>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        
        applicationScope.launch {
            insertInitialData()
        }
    }

    private suspend fun insertInitialData() {
        val timerDao = timerDaoProvider.get()

        val initialTimer =TimerBase.Initial(
            id = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            expectedDuration = DEFAULT_TIMER_DURATION,
        ).toEntity()

        timerDao.insertTimer(initialTimer)
    }
}
