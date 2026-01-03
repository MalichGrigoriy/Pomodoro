package com.timemanager.pomodorofocus.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
fun <T1, T2> Flow<T1>.waitForNext(other: Flow<T2>): Flow<Pair<T1, T2>> = channelFlow {

    val otherChannel = other.produceIn(this)

    this@waitForNext.transformLatest { valueFromThis ->

        while (otherChannel.tryReceive().isSuccess) {
            //to clear other chanel
        }
        val nextValueFromOther = otherChannel.receive()
        emit(valueFromThis to nextValueFromOther)
    }.collect(::send)
}

/**
 * @Deprecated This function has a potential race condition. If the `other` flow emits a value
 * before the receiver flow emits its first value, that emission from `other` will be lost.
 * Use [waitForNext] instead for a more robust implementation.
 *
 * Combines two flows by pairing the latest value from the receiver flow with the *next* value
 * from the `other` flow.
 *
 * This operator collects values from both flows concurrently. When the `other` flow emits a value,
 * it checks if a value has already been received from the receiver flow. If so, it combines
 * the latest value from the receiver and the new value from `other` using the provided `transform`
 * function, emits the result, and then waits for a new value from the receiver.
 *
 * @param other The second flow to combine with.
 * @param transform A function to combine the latest value from the receiver flow (`T1`) and the next
 * value from the `other` flow (`T2`) into a result (`R`).
 * @return A new flow that emits the results of the `transform` function.
 */
@Deprecated ("race condition problem use @waitForNext" )
fun <T1, T2> Flow<T1>.myWaitForNext(other: Flow<T2>): Flow<Pair<T1, T2>> =
    myWaitForNext(other) { value1, value2 -> Pair(value1, value2) }

@Deprecated ("race condition problem" )
@OptIn(ExperimentalCoroutinesApi::class)
fun <T1, T2, R> Flow<T1>.myWaitForNext(other: Flow<T2>, transform: suspend (T1, T2) -> R): Flow<R> = channelFlow {

    var lastValue1: Any? = null
    var hasValue1 = false

    launch {
        collect { value ->
            lastValue1 = value
            hasValue1 = true
        }
    }

    launch {
        other.collect { value ->
            if (hasValue1) {
                hasValue1 = false
                send(transform(lastValue1 as T1, value))
            }
        }
    }
}
