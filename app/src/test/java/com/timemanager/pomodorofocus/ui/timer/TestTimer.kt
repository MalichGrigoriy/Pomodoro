package com.timemanager.pomodorofocus.ui.timer

import com.timemanager.pomodorofocus.domain.timer.StopTimerUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.UUID

private const val AUTO_STOP_TIMER_DURATION = 2000L // 2 seconds

sealed class TestTimerState {
    data object Idle : TestTimerState()
    data class Running(val id: UUID) : TestTimerState()
    data object Stopped : TestTimerState()
}

@ExperimentalCoroutinesApi
class AutostopLogicTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val stopTimerUseCase: StopTimerUseCase = mock()

    private lateinit var _currentTimer: MutableStateFlow<TestTimerState>
    private lateinit var _timerMillis: MutableStateFlow<Long>
    private val testTimerId = UUID.randomUUID()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        _currentTimer = MutableStateFlow(TestTimerState.Idle)
        _timerMillis = MutableStateFlow(5000L)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `autostop with combine - call stopTimer many times `() = testScope.runTest {
        println("--- Starting test for two timer runs ---")

        val job = combine(_currentTimer, _timerMillis) { state, millis ->
            if (state is TestTimerState.Running && millis <= -AUTO_STOP_TIMER_DURATION) {
                stopTimerUseCase.autoStop(state.id)
            }
        }.launchIn(this)

        runAndVerifySingleCycle("First", 5000L, 101)

        job.cancel()
    }

    @Test
    fun `autostop with combine - call stopTimer many times each time`() = testScope.runTest {
        println("--- Starting test for two timer runs ---")

        val job = combine(_currentTimer, _timerMillis) { state, millis ->
            if (state is TestTimerState.Running && millis <= -AUTO_STOP_TIMER_DURATION) {
                stopTimerUseCase.autoStop(state.id)
            }
        }.launchIn(this)

        runAndVerifySingleCycle("First", 5000L, 101)
        runAndVerifySingleCycle("First", 4000L, 202)

        job.cancel()
    }

    @Test
    fun `autostop with flatMapLatest should call stopTimer on each run`() = testScope.runTest {
        println("--- Starting test for flatMapLatest ---")

        val job = _currentTimer
            .filter { it is TestTimerState.Running }
            .flatMapLatest { timerState ->
                val runningState = timerState as TestTimerState.Running
                _timerMillis
                    .filter { it <= -AUTO_STOP_TIMER_DURATION }
                    .take(1)
                    .onEach { stopTimerUseCase.autoStop(runningState.id) }
            }.launchIn(this)

        runAndVerifySingleCycle("First", 5000L, 1)
        runAndVerifySingleCycle("Second", 4000L, 2)

        job.cancel()
    }

    @Test
    fun `autostop with pairedCombine should call stopTimer on each run`() = testScope.runTest {
        println("--- Starting test for pairedCombine ---")

        val job = _currentTimer
            .filterIsInstance<TestTimerState.Running>()
            .pairedCombine(_timerMillis.filter { it <= -AUTO_STOP_TIMER_DURATION })
            .onEach { (runningState, _) -> stopTimerUseCase.autoStop(runningState.id) }
            .launchIn(this)

        runAndVerifySingleCycle("First", 5000L, 1)
        runAndVerifySingleCycle("Second", 4000L, 2)

        job.cancel()
    }

    private suspend fun TestScope.runAndVerifySingleCycle(
        name: String,
        duration: Long,
        expectedTimes: Int
    ) {
        println("--- $name run starting ---")
        startTimer(duration)
        simulateTimePassingUntil(-3000L)
        advanceUntilIdle()

        verify(stopTimerUseCase, times(expectedTimes)).autoStop(testTimerId)
        println("--- $name run verified successfully ---")

        _currentTimer.value = TestTimerState.Stopped
        advanceUntilIdle()
    }

    private fun startTimer(duration: Long = 5000L) {
        println("Action: Timer starting with duration ${duration}ms...")
        _currentTimer.value = TestTimerState.Running(testTimerId)
        _timerMillis.value = duration
    }

    private suspend fun simulateTimePassingUntil(targetMillis: Long) {
        println("Action: Simulating time passing until ${targetMillis}ms...")
        var currentTime = _timerMillis.value
        while (currentTime > targetMillis) {
            delay(10)
            currentTime -= 10
            _timerMillis.value = currentTime
        }
        println("Final time: ${_timerMillis.value}ms")
    }

}

fun <T1, T2> Flow<T1>.pairedCombine(other: Flow<T2>): Flow<Pair<T1, T2>> {
    return this.pairedCombine(other) { firstValue, secondValue -> Pair(firstValue, secondValue) }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun <T1, T2, R> Flow<T1>.pairedCombine(other: Flow<T2>, transform: (T1, T2) -> R): Flow<R> = flow {
    coroutineScope {
        val firstChannel = produce { this@pairedCombine.collect { element -> send(element) } }
        val secondChannel = produce { other.collect { element -> send(element) } }

        while (!firstChannel.isClosedForReceive && !secondChannel.isClosedForReceive) {
            try {
                val firstValue = firstChannel.receive()
                val secondValue = secondChannel.receive()
                emit(transform(firstValue, secondValue))
            } catch (e: Exception) {
            }
        }
    }
}
