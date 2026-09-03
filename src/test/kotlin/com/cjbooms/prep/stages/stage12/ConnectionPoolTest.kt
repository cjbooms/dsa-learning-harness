package com.cjbooms.prep.stages.stage12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Tests for the semaphore-based connection pool.
 *
 * Validates the bound (no more than maxConnections concurrent holders),
 * the reuse contract (idle connections are returned to the pool), and
 * the blocking behaviour when the pool is exhausted.
 */
class ConnectionPoolTest {

    @Test
    @Timeout(10)
    fun `lease returns a connection and release returns it`() {
        val opens = AtomicInteger(0)
        val pool = ConnectionPool(maxConnections = 2) { ConnectionPool.Connection(opens.incrementAndGet()) }

        val first = pool.lease()
        val second = pool.lease()
        assertNotNull(first)
        assertNotNull(second)
        assertEquals(2, opens.get(), "second lease should reuse an idle connection, not open a new one")

        pool.release(first)
        pool.release(second)
    }

    @Test
    @Timeout(10)
    fun `pool reuses idle connection rather than opening a new one`() {
        val opens = AtomicInteger(0)
        val pool = ConnectionPool(maxConnections = 2) { ConnectionPool.Connection(opens.incrementAndGet()) }

        val a = pool.lease()
        pool.release(a)
        val b = pool.lease()

        assertEquals(1, opens.get(), "second lease must reuse the released connection")
        assertEquals(a.id, b.id, "same connection should come back")

        pool.release(b)
    }

    @Test
    @Timeout(10)
    fun `lease blocks once pool is exhausted until release`() {
        val opens = AtomicInteger(0)
        val pool = ConnectionPool(maxConnections = 1) { ConnectionPool.Connection(opens.incrementAndGet()) }

        val held = pool.lease()

        val thirdLeaseReturned = AtomicInteger(0)
        val waiterStarted = CountDownLatch(1)
        val executor = Executors.newSingleThreadExecutor()

        val waiter = executor.submit {
            waiterStarted.countDown()
            val c = pool.lease()   // MUST block: pool is at capacity
            thirdLeaseReturned.set(1)
            pool.release(c)
        }

        assertTrue(waiterStarted.await(2, TimeUnit.SECONDS))
        Thread.sleep(200)
        assertEquals(0, thirdLeaseReturned.get(), "waiter should still be blocked")

        pool.release(held)          // frees a permit, unblocks the waiter
        waiter.get(3, TimeUnit.SECONDS)
        assertEquals(1, thirdLeaseReturned.get())

        executor.shutdownNow()
    }

    @Test
    @Timeout(15)
    fun `concurrent leases never exceed maxConnections`() {
        val maxConnections = 3
        val opens = AtomicInteger(0)
        val pool = ConnectionPool(maxConnections) { ConnectionPool.Connection(opens.incrementAndGet()) }

        val inFlight = AtomicInteger(0)
        val peak = AtomicInteger(0)
        val tasks = 20
        val start = CountDownLatch(1)
        val done = CountDownLatch(tasks)
        val executor = Executors.newFixedThreadPool(tasks)

        repeat(tasks) {
            executor.submit {
                start.await()
                val conn = pool.lease()
                try {
                    val now = inFlight.incrementAndGet()
                    peak.updateAndGet { p -> maxOf(p, now) }
                    Thread.sleep(20)
                } finally {
                    inFlight.decrementAndGet()
                    pool.release(conn)
                }
                done.countDown()
            }
        }

        start.countDown()
        assertTrue(done.await(10, TimeUnit.SECONDS))
        executor.shutdownNow()

        assertTrue(
            peak.get() <= maxConnections,
            "peak in-flight ${peak.get()} must not exceed maxConnections $maxConnections"
        )
        // And at least 2 holders must have overlapped — otherwise the bound is meaningless.
        assertTrue(peak.get() >= 2, "pool must allow more than one concurrent holder")
    }
}
