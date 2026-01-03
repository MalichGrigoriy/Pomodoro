package com.timemanager.pomodorofocus.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FlowExtensionTest {

    /*
    * 0ms:   flow1: ---          flow2: (1)          result: ---
    * 100ms: flow1: ---          flow2: (2)          result: ---
    * 150ms: flow1: (A)          flow2: ---          result: ---
    * 200ms: flow1: ---          flow2: (3)          result: ("A", 3)
    * 300ms: flow1: ---          flow2: (4)          result: ---
    * 330ms: flow1: (B)          flow2: ---          result: ---
    * 350ms: flow1: (C)          flow2: ---          result: ---
    * 400ms: flow1: ---          flow2: (5)          result: ("C", 5)
    * 450ms: flow1: (D)          flow2: ---          result: ---
    * 500ms: flow1: ---          flow2: (6)          result: ("D", 6)
    * [("A", 3), ("C", 5), ("D", 6)]
    */

    @Test
    fun `myWaitForNext test`() = runTest {
        val flow1 = flow {
            delay(150)
            emit("A")
            delay(180)
            emit("B")
            delay(20)
            emit("C")
            delay(100)
            emit("D")
        }


        val flow2 = flow {
            emit(1)
            delay(100)
            emit(2)
            delay(100)
            emit(3)
            delay(100)
            emit(4)
            delay(100)
            emit(5)
            delay(100)
            emit(6)
            delay(100)
            emit(7)
            delay(100)
            emit(8)
            delay(100)
            emit(9)
            delay(100)
            emit(10)
        }

        val result = flow1.myWaitForNext(flow2).toList()

        println("Result: $result")

        assertEquals(
            listOf("A" to 3, "C" to 5, "D" to 6),
            result
        )
    }

    @Test
    fun `waitForNext safety test`() = runTest {
        val flow1 = flow {
            delay(150)
            emit("A")
            delay(180)
            emit("B")
            delay(20)
            emit("C")
            delay(100)
            emit("D")
        }


        val flow2 = flow {
            emit(1)
            delay(100)
            emit(2)
            delay(100)
            emit(3)
            delay(100)
            emit(4)
            delay(100)
            emit(5)
            delay(100)
            emit(6)
            delay(100)
            emit(7)
            delay(100)
            emit(8)
            delay(100)
            emit(9)
            delay(100)
            emit(10)
        }

        val result = flow1.waitForNext(flow2).toList()

        println("Result: $result")

        assertEquals(
            listOf("A" to 3, "C" to 5, "D" to 6),
            result
        )
    }

}
