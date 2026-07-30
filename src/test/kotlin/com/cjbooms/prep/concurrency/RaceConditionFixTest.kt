package com.cjbooms.prep.concurrency

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RaceConditionFixTest {

    private fun hammer(increment: () -> Unit, threads: Int = 8, perThread: Int = 10_000) {
        val start = CountDownLatch(1)
        val done = CountDownLatch(threads)
        val pool = Executors.newFixedThreadPool(threads)
        repeat(threads) {
            pool.submit {
                start.await()
                repeat(perThread) { increment() }
                done.countDown()
            }
        }
        start.countDown()
        assertTrue(done.await(15, TimeUnit.SECONDS))
        pool.shutdownNow()
    }

    @Test
    @Timeout(20)
    fun `atomic counter is exact under contention`() {
        val counter = AtomicCounter()
        hammer(counter::increment)
        assertEquals(80_000, counter.count())
    }

    @Test
    @Timeout(20)
    fun `locked counter is exact under contention`() {
        val counter = LockedCounter()
        hammer(counter::increment)
        assertEquals(80_000, counter.count())
    }

    @Test
    @Timeout(20)
    fun `broken counter usually loses updates`() {
        // Not a correctness gate (races are timing-dependent) — demonstrates the bug.
        val counter = BrokenCounter()
        hammer(counter::increment, threads = 8, perThread = 10_000)
        println("BrokenCounter ended at ${counter.count} / 80000 (lost updates: ${80_000 - counter.count})")
    }

    @Test
    @Timeout(20)
    fun `locked bank account never overdraws`() {
        val account = LockedBankAccount(1_000)
        val start = CountDownLatch(1)
        val done = CountDownLatch(2)

        val pool = Executors.newFixedThreadPool(2)
        repeat(2) {
            pool.submit {
                start.await()
                repeat(1_000) { account.withdraw(1) }
                done.countDown()
            }
        }
        start.countDown()
        assertTrue(done.await(15, TimeUnit.SECONDS))
        pool.shutdownNow()
        assertEquals(0, account.balance())
    }
}
