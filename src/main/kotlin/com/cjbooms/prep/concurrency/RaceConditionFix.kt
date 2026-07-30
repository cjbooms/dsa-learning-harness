package com.cjbooms.prep.concurrency

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * "Find and fix the race condition" — a MongoDB-style round where you're shown
 * broken code and must (a) spot the race, (b) explain the interleaving that breaks
 * it, (c) fix it, (d) discuss alternatives and their trade-offs.
 */

/** BROKEN: read-modify-write is not atomic. Two threads can both read 5, both
 *  write 6 — a lost update. Also `count` is not volatile, so visibility across
 *  threads is not guaranteed by the JMM even without the atomicity bug. */
class BrokenCounter {
    var count = 0
        private set

    fun increment() {
        count++ // count = count + 1 — NOT atomic
    }
}

/** Fix option 1: AtomicInteger — lock-free CAS, best for a single counter. */
class AtomicCounter {
    private val count = AtomicInteger(0)

    fun increment() {
        count.incrementAndGet()
    }

    fun count(): Int = count.get()
}

/** Fix option 2: explicit lock — generalizes to multi-field invariants. */
class LockedCounter {
    private val lock = ReentrantLock()
    private var count = 0

    fun increment() = lock.withLock { count++ }

    fun count(): Int = lock.withLock { count }
}

/** BROKEN: check-then-act race. Two threads can both observe sufficient balance
 *  and both withdraw, overdrawing the account. */
class BrokenBankAccount(var balance: Long) {
    fun withdraw(amount: Long): Boolean {
        if (balance >= amount) {     // check
            balance -= amount        // act — another thread may have withdrawn in between
            return true
        }
        return false
    }
}

/** Fixed: the whole check-then-act must be inside the critical section. */
class LockedBankAccount(private var balance: Long) {
    private val lock = ReentrantLock()

    fun withdraw(amount: Long): Boolean = lock.withLock {
        if (balance >= amount) {
            balance -= amount
            true
        } else {
            false
        }
    }

    fun balance(): Long = lock.withLock { balance }
}
