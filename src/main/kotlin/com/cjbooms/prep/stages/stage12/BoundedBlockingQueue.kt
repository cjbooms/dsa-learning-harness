package com.cjbooms.prep.stages.stage12

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.concurrent.write

/**
 * Learn first: see docs/learning-resources.md
 * Producer/consumer bounded blocking queue.
 *
 * Implement a FIFO queue with a fixed [capacity]. Producers block when the
 * queue is full; consumers block when the queue is empty. The queue must be
 * safe for use by many concurrent producers and many concurrent consumers.
 *
 * Constructor parameter:
 *  - [capacity]: the maximum number of items the queue can hold. MUST be
 *    positive.
 *
 * Behavior:
 *  - `put(item)` blocks the calling thread until a slot is free, then appends
 *    the item.
 *  - `take()` blocks the calling thread until an item is available, then
 *    removes and returns the head item.
 *  - `size` returns the current number of items in the queue without
 *    blocking.
 */
class BoundedBlockingQueue<T>(private val capacity: Int) {


    private val queue = ArrayDeque<T>(capacity)
    private val count = java.util.concurrent.atomic.AtomicInteger(0)

    val lock = ReentrantLock()
    val readers = lock.newCondition()
    val writers = lock.newCondition()

    /**
     * Block until a slot is free, then append [item].
     *
     * @param item the value to enqueue.
     */
    fun put(item: T) {
        lock.withLock {
            while (queue.size >= capacity) {
               writers.await()
            }
            queue.addLast(item)
            count.incrementAndGet()
            readers.signal()
        }
    }

    /**
     * Block until an item is available, then remove and return the head.
     *
     * @return the head item.
     */
    fun take(): T {
        lock.withLock {
            while (queue.isEmpty()) {
                readers.await()
            }
            val value = queue.removeFirst()
            count.decrementAndGet()
            writers.signal()
            return value
        }
    }

    /**
     * @return the current number of items in the queue.
     */
    val size: Int
        get() {
            return count.get()
        }

}

fun main() {
    data class Test(val case: String, val expected: String?, val actual: String?) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }
    val cut = BoundedBlockingQueue<String>(3)
    cut.put("one")
    Test(
        case = "Put and Take work ",
        expected = "one",
        actual = cut.take()
    )

    // 1. Start a background consumer thread that should block
    val consumerThread = thread {
        Test(
            case = "Take with zero items, should wait until item added",
            expected = "future-added-item",
            actual = cut.take()
        )
    }

    Thread.sleep(1000)
    println("WAITING")
    println("WAITING")
    println("WAITING")
    println("WAITING")
    cut.put("future-added-item")


}

