package com.cjbooms.prep.stages.stage15

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Stage 15.6 — Cold-recall rebuild: read-write lock.
 *
 * MongoDB framing: storage engine shared-buffer access — many readers scan
 * pages concurrently, writers flush dirty pages exclusively. Same shape as
 * the Verified Stage 2 onsite question (Aug 2025).
 *
 * Rules:
 *   - readLock()    : many threads may hold concurrently
 *   - writeLock()   : exclusive — no readers, no other writer
 *   - readUnlock() / writeUnlock() paired with the matching acquire
 *   - Must be built from ReentrantLock + Condition(s) — NOT from
 *     java.util.concurrent.locks.ReentrantReadWriteLock
 *
 * State you'll need: reader count, active-writer flag, waiting-writer count.
 * Policy: writer preference (new readers block while a writer is waiting).
 *
 * Ritual before coding (speak aloud):
 *   1. ONE condition or TWO? Which is enough?
 *   2. The wait predicate for readers (when to block)? For writers?
 *   3. Why writer preference here — what's the cost?
 *
 * Time budget: 8 minutes cold.
 */
class RwLockRecall {


    fun readLock() {
        TODO("implement")
    }

    fun readUnlock() {
        TODO("implement")
    }

    fun writeLock() {
        TODO("implement")
    }

    fun writeUnlock() {
        TODO("implement")
    }
}
