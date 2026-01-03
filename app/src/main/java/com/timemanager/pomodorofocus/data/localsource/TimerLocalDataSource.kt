package com.timemanager.pomodorofocus.data.localsource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import com.timemanager.pomodorofocus.ui.timer.simpleView.timerTypeByName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerLocalDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val TIMER_KEY = stringPreferencesKey("timer_type")

    fun getTimerType(): Flow<TimerType> {
        return dataStore.data.map { preferences ->
            val type: String = preferences[TIMER_KEY] ?: TimerType.String.name
            timerTypeByName(type)
        }
    }

    suspend fun setTimerType(timerType: TimerType) {
        dataStore.edit { it[TIMER_KEY] = timerType.name }
    }
}
