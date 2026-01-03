package com.timemanager.pomodorofocus.data.repository

import com.timemanager.pomodorofocus.data.localsource.TimerLocalDataSource
import com.timemanager.pomodorofocus.domain.settings.SettingsRepository
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val localDataSource: TimerLocalDataSource) :
    SettingsRepository {
    override fun getTimerType(): Flow<TimerType> {
        return localDataSource.getTimerType()
    }

    override suspend fun setTimerType(timerType: TimerType) {
        localDataSource.setTimerType(timerType)
    }
}
