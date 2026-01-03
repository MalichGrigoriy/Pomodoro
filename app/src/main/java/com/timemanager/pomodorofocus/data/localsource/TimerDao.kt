package com.timemanager.pomodorofocus.data.localsource

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.timemanager.pomodorofocus.data.localsource.entity.TimerEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TimerDao {
    @Query("SELECT * FROM timers")
    fun getTimersFlow(): Flow<List<TimerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimers(timers: List<TimerEntity>)

    @Query("SELECT * FROM timers WHERE id = :id LIMIT 1")
    suspend fun getTimerById(id: UUID): TimerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: TimerEntity)

    @Update
    suspend fun updateTimer(timer: TimerEntity)

    @Query("SELECT * FROM timers ORDER BY startTime DESC")
    fun getPagedTimer(): PagingSource<Int, TimerEntity>
}
