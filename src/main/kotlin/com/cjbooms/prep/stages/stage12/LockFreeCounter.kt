package com.cjbooms.prep.stages.stage12

/**
 * Stage 12.3 — Lock-free counter using compare-and-set.
 * Time budget: 10 min.
 *
 * MongoDB context: hot counters are everywhere in a database engine —
 * operations executed, documents inserted, conflicts resolved, page faults
 * served. Under heavy contention a `synchronized` counter serialises every
 * increment onto one OS mutex. An AtomicLong with a CAS loop gives
 * wait-free progress: every thread retries on its own register and the
 * kernel scheduler never parks it.
 *
 * Structure-selection ritual (say aloud before coding):
 *   1. Primitive    -> AtomicLong (8 bytes on 64-bit JVM, lock-free writes).
 *                      Avoid AtomicInteger here so the exercise includes a
 *                      typed accumulator; both share the same CAS logic.
 *   2. CAS shape    -> do { expected = get(); } while (!compareAndSet(expected, expected + 1));
 *                      The loop absorbs concurrent updates without a lock.
 *   3. ABA risk     -> none for a monotonically increasing counter; mention
 *                      it explicitly because interviewers will ask.
 *   4. Why not synchronized? -> uncontended CAS is ~1ns; uncontended
 *      monitor enter is ~20ns. Under contention CAS wins by orders of
 *      magnitude because no thread ever blocks.
 *   5. Trade-off    -> CAS can starve under sustained contention (retry loop)
 *      and isn't applicable to multi-word state. Name both out loud.
 *
 * Follow-ups:
 *   - "Make it striped across N counters, sum on read?" -> sharded counters
 *     for very hot paths (LongAdder does exactly this).
 *   - "Add a decrement that fails below zero?" -> CAS until value > 0, then
 *     decrement.
 *   - "Why not volatile long?" -> ++ isn't atomic on a long; CAS is.
 */
class LockFreeCounter {

    /**
     * Atomically add [delta] and return the new value.
     * MUST be safe under many concurrent callers.
     */
    fun incrementAndGet(delta: Long = 1): Long {
        TODO("implement")
    }

    /** Current value (relaxed read). */
    fun get(): Long {
        TODO("implement")
    }

    /**
     * Atomically reset to zero and return the previous value.
     * Useful for "ops since last snapshot" telemetry.
     */
    fun getAndReset(): Long {
        TODO("implement")
    }
}
