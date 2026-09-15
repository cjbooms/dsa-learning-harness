package com.cjbooms.prep.solutions.stage12

import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

/**
 * Stage 12.3 — Lock-free counter using compare-and-set.
 * Time budget: 10 min.
 *
 * Why this matters: hot counters are everywhere in a database engine —
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
 *   2. CAS shape    -> do { expected = get(); } while (!compareAndSet(expected, expected + delta));
 *                      The loop absorbs concurrent updates without a lock.
 *                      updateAndGet on the JDK is implemented as exactly this
 *                      loop, so we get the same semantics inline.
 *   3. ABA risk     -> none for a monotonically increasing counter; mention
 *                      it explicitly because interviewers will ask.
 *   4. Why not synchronized? -> uncontested CAS is ~1ns; uncontested monitor
 *      enter is ~20ns. Under contention CAS wins by orders of magnitude
 *      because no thread ever blocks.
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

    private val value = AtomicLong(0)

    /**
     * Atomically add [delta] and return the new value.
     * MUST be safe under many concurrent callers.
     */
    fun incrementAndGet(delta: Long = 1): Long {
        // CAS retry loop — every thread spins on its own register, no
        // OS-level parking, no monitor contention. updateAndGet does
        // exactly this on the JDK side.
        return value.updateAndGet { current -> current + delta }
    }

    /** Current value (relaxed read). */
    fun get(): Long = value.get()

    /**
     * Atomically reset to zero and return the previous value.
     * Useful for "ops since last snapshot" telemetry.
     */
    fun getAndReset(): Long = value.getAndSet(0L)
}

fun main() {
    data class Test(val case: String, val expected: Long, val actual: Long) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // deterministic single-thread checks
    val counter = LockFreeCounter()
    Test("Initial value is zero", 0L, counter.get())
    Test("First increment returns 1", 1L, counter.incrementAndGet())
    Test("Default delta is 1", 2L, counter.incrementAndGet())
    Test("Custom delta accumulates", 7L, counter.incrementAndGet(5))
    Test("Get after increments", 7L, counter.get())
    Test("getAndReset returns previous value", 7L, counter.getAndReset())
    Test("Counter is zero after reset", 0L, counter.get())
    Test("Increment after reset starts fresh", 10L, counter.incrementAndGet(10))

    // one small threaded case: many threads each call incrementAndGet(1)
    // N times — final value must equal threads * N with no lost updates.
    val sharedCounter = LockFreeCounter()
    val threads = 8
    val perThread = 1_000
    val workers = (1..threads).map {
        thread {
            repeat(perThread) { sharedCounter.incrementAndGet() }
        }
    }
    workers.forEach { it.join() }
    Test(
        case = "Threaded increments produce no lost updates",
        expected = (threads * perThread).toLong(),
        actual = sharedCounter.get(),
    )
}
