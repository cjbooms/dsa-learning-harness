package com.cjbooms.prep.stages.stage2

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ReadWriteLockTest {

    @Test
    @Timeout(10)
    fun `readers proceed concurrently`() {
        val rw = SimpleReadWriteLock()
        val readersInside = AtomicInteger(0)
        val maxConcurrent = AtomicInteger(0)
        val start = CountDownLatch(1)
        val done = CountDownLatch(4)

        val pool = Executors.newFixedThreadPool(4)
        repeat(4) {
            pool.submit {
                start.await()
                rw.readLock()
                val now = readersInside.incrementAndGet()
                maxConcurrent.updateAndGet { m -> maxOf(m, now) }
                readersInside.decrementAndGet()
                rw.readUnlock()
                done.countDown()
            }
        }
        start.countDown()
        assertTrue(done.await(5, TimeUnit.SECONDS))
        pool.shutdownNow()
        assertTrue(maxConcurrent.get() > 1, "readers should overlap")
    }

    @Test
    @Timeout(10)
    fun `writer is exclusive and readers wait`() {
        val rw = SimpleReadWriteLock()
        val activeReaders = AtomicInteger(0)
        val activeWriter = AtomicInteger(0)
        val violations = AtomicInteger(0)
        val start = CountDownLatch(1)
        val done = CountDownLatch(6)

        val pool = Executors.newFixedThreadPool(6)
        repeat(4) {
            pool.submit {
                start.await()
                rw.readLock()
                if (activeWriter.get() > 0) violations.incrementAndGet()
                activeReaders.incrementAndGet(); activeReaders.decrementAndGet()
                rw.readUnlock()
                done.countDown()
            }
        }
        repeat(2) {
            pool.submit {
                start.await()
                rw.writeLock()
                if (activeWriter.incrementAndGet() > 1) violations.incrementAndGet()
                if (activeReaders.get() > 0) violations.incrementAndGet()
                activeWriter.decrementAndGet()
                rw.writeUnlock()
                done.countDown()
            }
        }
        start.countDown()
        assertTrue(done.await(5, TimeUnit.SECONDS))
        pool.shutdownNow()
        assertEquals(0, violations.get())
    }
}
