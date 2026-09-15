package com.cjbooms.prep.stages.stage2

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Stage 2 — Implement a read-write lock (classic concurrency interview question).
 *
 * Rules:
 *   - readLock(): many threads may hold the read lock simultaneously
 *   - writeLock(): exclusive — no readers, no other writer
 *   - Must be built from ReentrantLock + condition(s) — not from
 *     ReentrantReadWriteLock (that's the whole point of the exercise)
 *
 * State you'll need: active reader count, active writer flag, waiting writer
 * count. Decide: ONE condition or TWO? Defend aloud.
 *
 * Policy (2.2 in the stage doc): when a writer is WAITING, should new readers
 * be admitted? Implement writer-preference. Then say aloud what each policy
 * costs: writer-preference -> reader throughput; reader-preference -> writer
 * starvation.
 */
class SimpleReadWriteLock {

    val lock = ReentrantLock()
    var readers = 0
    var activeWriter = false
    var waitingWriters = 0
    val condition = lock.newCondition()

    fun readLock() {
        lock.withLock {
            while (activeWriter || waitingWriters > 0) {
                condition.await()
            }
            readers++
        }
    }

    fun readUnlock() {
        lock.withLock {
            readers--
            if (readers == 0) condition.signalAll()
        }
    }

    fun writeLock() {
        lock.withLock {
            waitingWriters++
            while (readers > 0 || activeWriter) {
                condition.await()
            }
            waitingWriters--
            activeWriter = true
        }
    }

    fun writeUnlock() {
        lock.withLock {
            activeWriter = false
            condition.signalAll()
        }
    }
}
