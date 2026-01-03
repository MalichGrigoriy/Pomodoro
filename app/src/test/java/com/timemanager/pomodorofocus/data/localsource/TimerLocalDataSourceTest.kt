//package com.timemanager.pomodorofocus.data.localsource
//
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.MutablePreferences
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flowOf
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class TimerLocalDataSourceTest {
//
//    private val testTimerKey = stringPreferencesKey("timer_type")
//
//    @Test
//    fun `getTimerType should emit default value when key is not present`(): Unit = runTest {
//        // Arrange
//        val mockPreferences = mock(Preferences::class.java) // Або mockk<Preferences>()
//        `when`(mockPreferences[testTimerKey]).thenReturn(null) // Або coEvery { mockPreferences[testTimerKey] } returns null
//
//        val mockDataStore = mock(DataStore::class.java) as DataStore<Preferences> // Або mockk<DataStore<Preferences>>()
//        `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences)) // Або coEvery { mockDataStore.data } returns flowOf(mockPreferences)
//
//        val dataSource = TimerLocalDataSource(mockDataStore)
//
//        // Act
//        val timerType = dataSource.getTimerType().first()
//
//        // Assert
//        assertEquals(TimerType.String, timerType)
//    }
//
//    @Test
//    fun `getTimerType should emit stored value when key is present`(): Unit = runTest {
//        // Arrange
//        val storedType = TimerType.LedMatrix::class.simpleName
//        val mockPreferences = mock(Preferences::class.java) // Або mockk<Preferences>()
//        `when`(mockPreferences[testTimerKey]).thenReturn(storedType) // Або coEvery { mockPreferences[testTimerKey] } returns storedType
//
//        val mockDataStore = mock(DataStore::class.java) as DataStore<Preferences> // Або mockk<DataStore<Preferences>>()
//        `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences)) // Або coEvery { mockDataStore.data } returns flowOf(mockPreferences)
//
//        val dataSource = TimerLocalDataSource(mockDataStore)
//
//        // Act
//        val timerType = dataSource.getTimerType().first()
//
//        // Assert
//        assertEquals(TimerType.LedMatrix, timerType)
//    }
//
//    @Test
//    fun `setTimerType should store the provided timer type`() = runTest {
//        // Arrange
//        val timerToStore = TimerType.Dual
//        val expectedStoredValue = timerToStore::class.simpleName
//
//        val mutablePreferences = mutableMapOf<Preferences.Key<*>, Any?>()
//
//        val mockDataStore = mock(DataStore::class.java) as DataStore<Preferences> // Або mockk<DataStore<Preferences>>()
//        `when`(mockDataStore.data).thenReturn(flowOf(mock(Preferences::class.java))) // Повертаємо порожній Flow для data
//        `when`(mockDataStore.edit(any())).thenAnswer { invocation -> // Або coEvery { mockDataStore.edit(any()) } coAnswers { ... }
//            val transform = invocation.arguments[0] as suspend (MutablePreferences) -> Unit
//            val mutablePreferencesImpl = mutablePreferences.toMutablePreferences()
//            transform(mutablePreferencesImpl)
//            flowOf(mutablePreferencesImpl.toPreferences())
//        }
//
//        val dataSource = TimerLocalDataSource(mockDataStore)
//
//        // Act
//        dataSource.setTimerType(timerToStore)
//
//        // Assert
//        assertEquals(expectedStoredValue, mutablePreferences[testTimerKey])
//    }
//}
//
//// Helper function to convert MutableMap to MutablePreferences (requires androidx.datastore:datastore-core)
//private fun MutableMap<Preferences.Key<*>, Any?>.toMutablePreferences(): MutablePreferences {
//    return object : MutablePreferences {
//        override fun <T> get(key: Preferences.Key<T>): T? = this@toMutablePreferences[key] as? T
//        override fun <T> set(key: Preferences.Key<T>, value: T) {
//            this@toMutablePreferences[key] = value
//        }
//        override fun <T> remove(key: Preferences.Key<T>) {
//            this@toMutablePreferences.remove(key)
//        }
//        override fun clear() {
//            this@toMutablePreferences.clear()
//        }
//        override val keys: Set<Preferences.Key<*>> = this@toMutablePreferences.keys
//    }
//}
//
//// Helper function to convert MutablePreferences to Preferences (requires androidx.datastore:datastore-core)
//private fun MutablePreferences.toPreferences(): Preferences {
//    return object : Preferences {
//        override fun <T> get(key: Preferences.Key<T>): T? = this@toPreferences[key] as? T
//        override val keys: Set<Preferences.Key<*>> = this@toPreferences.keys
//    }
//}
