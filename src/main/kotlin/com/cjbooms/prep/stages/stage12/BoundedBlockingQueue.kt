package com.cjbooms.prep.stages.stage12

/**
 * Stage 12.1 — Producer/consumer bounded blocking queue.
 * Time budget: 20 min.
 *
 * MongoDB context: bounded queues sit at the heart of MongoDB's executor
 * pools (e.g. the network thread pool feeding the operation scheduler,
 * or the WiredTiger cache eviction queue). When the queue is full the
 * producer must block, not drop — that's the bound. When it's empty
 * the consumer must block, not spin — that's the savings vs. polling.
 *
 * Structure-selection ritual (say aloud before coding):
 *   1. Lock choice  -> ONE ReentrantLock (the buffer + waiting predicates
 *                      are ONE critical section).
 *   2. Wait queues  -> TWO Conditions keyed off that lock:
 *                        - notFull  : producers wait here while size == cap
 *                        - notEmpty : consumers wait here while size == 0
 *                      Two conditions so we wake only the relevant side
 *                      (signal(), not signalAll()) — no thundering herd.
 *   3. Buffer       -> ArrayDeque<T> as a circular FIFO (addLast/removeFirst
 *                      are both O(1) and capacity is fixed by the queue, not
 *                      the deque).
 *   4. Await guard  -> `while (predicate)` not `if`. await() returns spuriously
 *                      and on condition mismatch — re-check every wakeup.
 *
 * Why not BlockingQueue from the JDK? The exercise IS to build it. Saying
 * "I'd just use ArrayBlockingQueue" is the wrong answer for a Staff round.
 *
 * Follow-ups to practice:
 *   - "Add offer(item, timeoutMs)"     -> notFull.awaitNanos(remainingNanos)
 *   - "What if multiple consumers?"    -> still signal() — exactly one wakes.
 *   - "Fair ordering for producers?"   -> ReentrantLock(fair = true).
 */
class BoundedBlockingQueue<T>(private val capacity: Int) {

    /**
     * Block until a slot is free, then append [item].
     * MUST be safe under many concurrent producers.
     */
    fun put(item: T) {
        TODO("implement")
    }

    /**
     * Block until an item is available, then return and remove the head.
     * MUST be safe under many concurrent consumers.
     */
    fun take(): T {
        TODO("implement")
    }

    /** Non-blocking size read for tests/observability. */
    val size: Int
        get() {
            TODO("implement")
        }
}
