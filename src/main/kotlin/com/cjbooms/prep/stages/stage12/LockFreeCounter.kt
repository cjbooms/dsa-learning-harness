package com.cjbooms.prep.stages.stage12

/**
 * Lock-free counter.
 *
 * Implement a counter that supports concurrent updates without using locks.
 * All operations must be safe under many concurrent callers.
 *
 * Behavior:
 *  - `incrementAndGet(delta)` atomically adds [delta] and returns the new
 *    value.
 *  - `get()` returns the current value (a relaxed read).
 *  - `getAndReset()` atomically resets the counter to zero and returns the
 *    previous value.
 */
class LockFreeCounter {

    /**
     * Atomically add [delta] to the counter and return the new value.
     * Must be safe under many concurrent callers.
     *
     * @param delta the amount to add. Defaults to 1.
     * @return the value of the counter after the update.
     */
    fun incrementAndGet(delta: Long = 1): Long {
        TODO("implement")
    }

    /**
     * @return the current value of the counter.
     */
    fun get(): Long {
        TODO("implement")
    }

    /**
     * Atomically reset the counter to zero and return its previous value.
     *
     * @return the value of the counter immediately before the reset.
     */
    fun getAndReset(): Long {
        TODO("implement")
    }

}
