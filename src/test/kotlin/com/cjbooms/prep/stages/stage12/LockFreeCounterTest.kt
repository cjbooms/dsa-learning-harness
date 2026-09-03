package com.cjbooms.prep.stages.stage12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * Tests for the lock-free counter.
 *
 * These exercise the correctness of the CAS loop (no lost updates under
 * contention) and the snapshot/reset semantics.
 */
class LockFreeCounterTest {

    @Test
    @Timeout(10)
    fun `incrementAndGet returns the new value`() {
        val c = LockFreeCounter()
        assertEquals(1L, c.incrementAndGet())
        assertEquals(2L, c.incrementAndGet())
        assertEquals(5L, c.incrementAndGet(3))
        assertEquals(5L, c.get())
    }

    @Test
    @Timeout(10)
    fun `getAndReset returns the prior value and zeroes the counter`() {
        val c = LockFreeCounter()
        c.incrementAndGet()
        c.incrementAndGet()
        c.incrementAndGet()
        assertEquals(3L, c.getAndReset())
        assertEquals(0L, c.get())
        // Subsequent increments behave like a fresh counter.
        assertEquals(1L, c.incrementAndGet())
    }

    @Test
    @Timeout(15)
    fun `concurrent increments never lose updates`() {
        val threads = 8
        val perThread = 50_000
        val total = threads * perThread

        val counter = LockFreeCounter()
        val barrier = CountDownLatch(1)
        val done = CountDownLatch(threads)
        val pool = Executors.newFixedThreadPool(threads)

        repeat(threads) {
            pool.submit {
                barrier.await()
                repeat(perThread) {
                    counter.incrementAndGet()
                }
                done.countDown()
            }
        }

        barrier.countDown()
        assertTrue(done.await(10, TimeUnit.SECONDS), "workers did not finish in time")
        pool.shutdownNow()

        assertEquals(
            total.toLong(),
            counter.get(),
            "CAS loop must absorb every concurrent increment with zero loss",
        )
    }

    @Test
    @Timeout(15)
    fun `concurrent increments with custom delta are exact`() {
        val threads = 4
        val perThread = 10_000
        val delta = 7L
        val expected = threads * perThread * delta

        val counter = LockFreeCounter()
        val barrier = CountDownLatch(1)
        val done = CountDownLatch(threads)
        val pool = Executors.newFixedThreadPool(threads)

        repeat(threads) {
            pool.submit {
                barrier.await()
                repeat(perThread) {
                    counter.incrementAndGet(delta)
                }
                done.countDown()
            }
        }

        barrier.countDown()
        assertTrue(done.await(10, TimeUnit.SECONDS))
        pool.shutdownNow()

        assertEquals(expected, counter.get())
    }
}
