package com.cjbooms.prep.stages.stage11

import kotlin.math.min

/**
 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 *
 * Implement the following mechanics:
 * - `push(val: Int)` pushes the element val onto the stack.
 * - `pop()` removes the element on the top of the stack.
 * - `top(): Int` gets the top element of the stack.
 * - `getMin(): Int` retrieves the minimum element in the stack.
 *
 * Constraint: You must implement a solution with O(1) time complexity for each function.
 */

class Stack {

    val list = ArrayDeque<Pair<Int, Int>>() // Value top current Min

    fun push(value: Int) {
        val newMin = if (list.isEmpty()) value else min(list.first().second, value)
        list.addFirst(value to newMin)
    }

    fun pop() {
        list.removeFirstOrNull()
    }

    fun top(): Int? {
        return list.firstOrNull()?.first
    }

    fun getMin(): Int? {
        return list.firstOrNull()?.second
    }


}


fun main() {
    data class Test(val case: String, val expected: Int?, val actual: Int?) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }
    val cut = Stack()
    cut.push(1)
    Test(
        case = "Single push shows in top",
        expected = 1,
        actual = cut.top()
    )
    Test(
        case = "Single push min shows in top",
        expected = 1,
        actual = cut.getMin()
    )
    cut.pop()
    Test(
        case = "Single push popped does not show in top",
        expected = null,
        actual = cut.top()
    )
    Test(
        case = "Single push popped does not show in top",
        expected = null,
        actual = cut.top()
    )
    cut.push(5)
    cut.push(3)
    cut.push(6)
    cut.push(Int.MIN_VALUE)
    cut.push(2)
    Test(
        case = "Most recent push shows in top",
        expected = 2,
        actual = cut.top()
    )
    Test(
        case = "Lowest min shows",
        expected = Int.MIN_VALUE,
        actual = cut.getMin()
    )
    cut.pop()
    cut.pop()
    Test(
        case = "Popping lowest min shows new lowest",
        expected = 3,
        actual = cut.getMin()
    )
}

/**
 * Implement a first-in-first-out (FIFO) queue using only two standard LIFO stacks.
 * The implemented queue should support all the functions of a normal queue.
 *
 * Implement the following mechanics:
 * - `push(x: Int)` Pushes element x to the back of the queue.
 * - `pop(): Int` Removes the element from the front of the queue and returns it.
 * - `peek(): Int` Returns the element at the front of the queue.
 * - `empty(): Boolean` Returns true if the queue is empty, false otherwise.
 *
 * Constraint: You must use strictly standard operations of a stack (push to top,
 * peek/pop from top, size, and is empty). You may simulate a stack using an ArrayDeque
 * as long as you strictly adhere to LIFO stack operations. Amortized O(1) time
 * complexity is expected for pop and peek.
 */


