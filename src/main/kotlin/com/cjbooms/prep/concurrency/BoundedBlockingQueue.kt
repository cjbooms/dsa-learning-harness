package com.cjbooms.prep.concurrency

import java.util.ArrayDeque
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Canonical concurrency interview question: producer/consumer bounded buffer.
 *
 * put() blocks while the queue is full; take() blocks while it is empty.
 *
 * Implementation: one ReentrantLock guarding the buffer, plus two Conditions:
 *   - notFull:  producers wait here while buffer.size == capacity
 *   - notEmpty: consumers wait here while buffer.isEmpty()
 *
 * The three things interviewers are listening for:
 *
 *  1. WHY `while` and not `if` around await(): await() can return without the
 *     condition being true (spurious wakeup, or another thread grabbed the slot
 *     between signal and re-acquire). Re-checking in a loop is the only safe form.
 *
 *  2. WHY two conditions instead of one: with a single condition you'd need
 *     signalAll() and would wake producers to tell them... it's still full.
 *     Separate conditions let put() wake exactly one consumer and take() wake
 *     exactly one producer — no thundering herd.
 *
 *  3. WHY signal() is sufficient here: all waiters on a given condition are
 *     homogeneous (only producers wait on notFull, only consumers on notEmpty),
 *     and each state change (one slot freed / one item added) can satisfy
 *     exactly one waiter.
 *
 * Follow-up mutations to practice (interviewers DO change the rules mid-round):
 *   - "add offer(item, timeoutMs)"                 -> notFull.awaitNanos(...)
 *   - "use this to bound an ExecutorService queue,
 *      so submit() blocks when full"               -> wrap take/put around the pool
 *   - "make wakeups fair (FIFO)"                   -> ReentrantLock(fair = true)
 */
class BoundedBlockingQueue<T>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    private val lock = ReentrantLock()

    // Producers park on notFull; consumers park on notEmpty.
    // Both conditions share the same lock — a Condition is always bound to the
    // lock that created it, and you must hold that lock to await/signal.
    private val notFull = lock.newCondition()
    private val notEmpty = lock.newCondition()

    // ArrayDeque as a circular buffer: addLast/removeFirst are O(1).
    private val buffer = ArrayDeque<T>(capacity)

    fun put(item: T) {
        lock.withLock {
            // while, not if: re-test the predicate after every wakeup.
            while (buffer.size == capacity) {
                notFull.await() // atomically releases the lock and parks
            }

            buffer.addLast(item)

            // One item is now available -> wake one waiting consumer (if any).
            notEmpty.signal()
        }
    }

    fun take(): T {
        lock.withLock {
            while (buffer.isEmpty()) {
                notEmpty.await()
            }

            val item = buffer.removeFirst()

            // One slot is now free -> wake one waiting producer (if any).
            notFull.signal()

            return item
        }
    }

    val size: Int
        get() = lock.withLock { buffer.size }
}

/**
 * Same semantics with the older synchronized/wait/notify idiom.
 *
 * Practice this version too — interviewers ask "how would you do it without
 * j.u.c.locks?", and the answer has a deliberate trap:
 *
 *   There is only ONE wait-set per monitor, shared by blocked producers AND
 *   consumers. If put() called notify() it might wake another PRODUCER (useless,
 *   and the consumer stays asleep forever -> deadlock). Hence notifyAll().
 *   That correctness-forced wakeup of everyone is exactly the inefficiency
 *   that two explicit Conditions eliminate.
 */
class SynchronizedBoundedBlockingQueue<T>(private val capacity: Int) {

    private val buffer = ArrayDeque<T>(capacity)

    @Synchronized
    fun put(item: T) {
        while (buffer.size == capacity) {
            // Kotlin maps wait()/notifyAll() onto java.lang.Object; the cast
            // and suppression are the accepted idiom for calling them.
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

/**
 * Third variant: two semaphores + a lock. Offer this after the lock/condition
 * version — comparing them aloud is exactly the Staff+ move.
 *
 * How it works:
 *   slotsAvailable starts at `capacity`  -> producers acquire before putting
 *   itemsAvailable starts at 0           -> consumers acquire before taking
 *   The two permit counts always sum to `capacity`, so the buffer can never
 *   overfill or overdrain. The lock is still required: semaphores COUNT,
 *   they don't make the deque itself thread-safe.
 *
 * Trade-offs vs lock+conditions (say these out loud):
 *   + Less code; no while-loop/signal placement to get wrong.
 *   + Timed operations are just tryAcquire(timeout) — easy offer(item, timeout).
 *   - Permits can only COUNT; conditions can express arbitrary predicates
 *     ("not full AND priority lane open"). If the follow-up adds a predicate,
 *     semaphores get awkward fast.
 *   - THE semaphore-specific bug: release() unconditionally ADDS a permit.
 *     A mismatched release silently grows capacity beyond the bound — the
 *     while-loop re-check in the lock version catches logic errors; a
 *     semaphore will not save you from yourself.
 */
class SemaphoreBoundedBlockingQueue<T>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    private val buffer = ArrayDeque<T>(capacity)

    // Permits for empty slots. Producers spend one per put; consumers refund
    // one per take. Starts at capacity = "all slots empty".
    private val slotsAvailable = java.util.concurrent.Semaphore(capacity)

    // Permits for filled slots. Consumers spend one per take; producers add
    // one per put. Starts at 0 = "nothing to take yet".
    private val itemsAvailable = java.util.concurrent.Semaphore(0)

    // Guards the deque itself — semaphore permits order WHO may proceed,
    // but concurrent addLast/removeFirst still need mutual exclusion.
    private val bufferLock = ReentrantLock()

    fun put(item: T) {
        slotsAvailable.acquire() // blocks while full
        bufferLock.withLock { buffer.addLast(item) }
        itemsAvailable.release() // one more item for consumers
    }

    fun take(): T {
        itemsAvailable.acquire() // blocks while empty
        val item = bufferLock.withLock { buffer.removeFirst() }
        slotsAvailable.release() // one more slot for producers
        return item
    }

    /** The follow-up made trivial: timed offer. */
    fun offer(item: T, timeoutMillis: Long): Boolean {
        val gotSlot = slotsAvailable.tryAcquire(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
        if (!gotSlot) return false
        bufferLock.withLock { buffer.addLast(item) }
        itemsAvailable.release()
        return true
    }
}
