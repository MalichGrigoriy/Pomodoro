package com.timemanager.pomodorofocus.data.localsource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timemanager.pomodorofocus.data.localsource.entity.TimerEntity

@Database(entities = [TimerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
