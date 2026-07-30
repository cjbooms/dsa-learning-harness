package com.cjbooms.prep.concurrency

import java.util.ArrayDeque
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Canonical MongoDB concurrency-round question: producer/consumer bounded buffer.
 *
 * put() blocks while full; take() blocks while empty.
 *
 * Implementation: single ReentrantLock + two Conditions (notFull / notEmpty).
 * Key invariants to be able to explain:
 *  - wait() releases the lock and must be in a `while` loop (spurious wakeups,
 *    and a signal does not guarantee the condition still holds when re-acquired).
 *  - signal() (not signalAll()) is safe here ONLY because each wakeup can make
 *    progress and we re-signal transitively... actually here we use signalAll-style
 *    reasoning: after take() the buffer is notFull; after put() it is notEmpty.
 *    signal() suffices since waiters on each condition are homogeneous.
 *
 * Follow-up mutations interviewers impose (practice all):
 *  - "now make put() offer a timeout"      -> notFull.awaitNanos(...)
 *  - "now support multiple item types"     -> per-type conditions or signalAll
 *  - "now bound an ExecutorService queue with blocking submit" -> wrap this in submit
 */
class BoundedBlockingQueue<T>(private val capacity: Int) {
    init {
        require(capacity > 0) { "capacity must be positive" }
    }

    private val lock = ReentrantLock()
    private val notFull = lock.newCondition()
    private val notEmpty = lock.newCondition()
    private val buffer = ArrayDeque<T>(capacity)

    fun put(item: T) {
        lock.withLock {
            while (buffer.size == capacity) {
                notFull.await()
            }
            buffer.addLast(item)
            notEmpty.signal()
        }
    }

    fun take(): T {
        lock.withLock {
            while (buffer.isEmpty()) {
                notEmpty.await()
            }
            val item = buffer.removeFirst()
            notFull.signal()
            return item
        }
    }

    val size: Int
        get() = lock.withLock { buffer.size }
}

/**
 * Same semantics with the older synchronized/wait/notify idiom — worth practicing
 * because interviewers sometimes ask "how would you do it without j.u.c.locks?".
 * Note: notifyAll (not notify) is required here because a single monitor mixes
 * put-waiters and take-waiters.
 */
class SynchronizedBoundedBlockingQueue<T>(private val capacity: Int) {
    private val buffer = ArrayDeque<T>(capacity)

    @Synchronized
    fun put(item: T) {
        while (buffer.size == capacity) {
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            (this as Object).wait()
        }
        buffer.addLast(item)
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        (this as Object).notifyAll()
    }

    @Synchronized
    fun take(): T {
        while (buffer.isEmpty()) {
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            (this as Object).wait()
        }
        val item = buffer.removeFirst()
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        (this as Object).notifyAll()
        return item
    }
}
