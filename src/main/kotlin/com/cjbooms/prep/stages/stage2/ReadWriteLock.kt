package com.cjbooms.prep.stages.stage2

/**
 * Stage 2 — Implement a read-write lock (VERIFIED MongoDB onsite question,
 * Aug 2025 — candidate solved it in ~30 min with follow-ups).
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

    fun readLock() {
        TODO("block while a writer is active — and while a writer WAITS? (policy!)")
    }

    fun readUnlock() {
        TODO("who do you signal, and when?")
    }

    fun writeLock() {
        TODO("block until no active readers AND no active writer")
    }

    fun writeUnlock() {
        TODO("who do you signal, and in what order?")
    }
}
