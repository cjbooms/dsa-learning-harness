package com.cjbooms.prep.stages.stage15

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * A reader-writer lock built from `ReentrantLock` and one or more `Condition`s
 * (not from `ReentrantReadWriteLock`).
 *
 * Multiple readers may hold the lock concurrently. A writer holds it
 * exclusively: while a writer holds the lock, no reader or other writer may
 * hold it. Newly arriving readers block while a writer is waiting. Every
 * successful `readLock()` must be paired with `readUnlock()`; every
 * successful `writeLock()` must be paired with `writeUnlock()`.
 */
class RwLockRecall {

    /**
     * Acquires the lock for reading; blocks if a writer currently holds the
     * lock or a writer is waiting.
     */
    fun readLock() {
        TODO("implement")
    }

    /**
     * Releases a previously acquired read lock.
     */
    fun readUnlock() {
        TODO("implement")
    }

    /**
     * Acquires the lock for writing; blocks while any readers or another
     * writer currently hold the lock.
     */
    fun writeLock() {
        TODO("implement")
    }

    /**
     * Releases a previously acquired write lock.
     */
    fun writeUnlock() {
        TODO("implement")
    }
}
