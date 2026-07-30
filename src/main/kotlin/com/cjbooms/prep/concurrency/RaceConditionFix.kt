package com.cjbooms.prep.concurrency

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * "Find and fix the race condition" — you're shown broken code and must:
 *   (a) spot the race,
 *   (b) name the exact interleaving that breaks it,
 *   (c) fix it,
 *   (d) discuss alternatives and their trade-offs.
 *
 * Two bug archetypes below: lost update (atomicity) and check-then-act.
 */

// ---------------------------------------------------------------------------
// Bug 1: the lost update
// ---------------------------------------------------------------------------

/** BROKEN. count++ looks like one operation but is three:
 *
 *     read count -> add 1 -> write count
 *
 *  Broken interleaving (both threads start at count = 5):
 *     T1: read 5
 *     T2: read 5
 *     T1: write 6
 *     T2: write 6          <- one increment is LOST
 *
 *  There's a second, subtler bug: `count` is not volatile and there's no
 *  synchronization, so the JMM doesn't even guarantee one thread ever SEES
 *  another's write (visibility). Atomicity and visibility are separate
 *  problems — name both.
 */
class BrokenCounter {
    var count = 0
        private set

    fun increment() {
        count++
    }
}

/** Fix option 1: AtomicInteger — hardware CAS (compare-and-swap), lock-free.
 *  Best choice for a single independent counter: no blocking, no contention
 *  bottleneck beyond the CAS retry loop. */
class AtomicCounter {
    private val count = AtomicInteger(0)

    fun increment() {
        count.incrementAndGet() // CAS loop: retries until the write wins
    }

    fun count(): Int = count.get()
}

/** Fix option 2: explicit lock. Right choice when the invariant spans MULTIPLE
 *  fields (e.g. count plus a max-value tracker) — atomics compose poorly. */
class LockedCounter {
    private val lock = ReentrantLock()
    private var count = 0

    fun increment() {
        lock.withLock { count++ }
    }

    fun count(): Int = lock.withLock { count }
}

// ---------------------------------------------------------------------------
// Bug 2: check-then-act
// ---------------------------------------------------------------------------

/** BROKEN. The balance check and the withdrawal are separate steps:
 *
 *     T1: check balance(100) >= 60  -> true
 *     T2: check balance(100) >= 60  -> true
 *     T1: balance = 40
 *     T2: balance = -20             <- overdrawn!
 *
 *  The check's result is STALE by the time the act happens. The fix is not
 *  a faster check — it's making check+act one atomic unit.
 */
class BrokenBankAccount(var balance: Long) {
    fun withdraw(amount: Long): Boolean {
        if (balance >= amount) {
            balance -= amount
            return true
        }
        return false
    }
}

/** Fixed: the entire check-then-act lives inside the critical section.
 *  Note the reader ALSO takes the lock — reading a mutable field without
 *  synchronization is itself a visibility bug. */
class LockedBankAccount(private var balance: Long) {
    private val lock = ReentrantLock()

    fun withdraw(amount: Long): Boolean {
        return lock.withLock {
            val hasSufficientFunds = balance >= amount
            if (hasSufficientFunds) {
                balance -= amount
            }
            hasSufficientFunds
        }
    }

    fun balance(): Long = lock.withLock { balance }
}
