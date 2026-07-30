package com.cjbooms.prep.concurrency

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadSafeLruCacheTest {

    @Test
    fun `evicts least recently used`() {
        val cache = ThreadSafeLruCache<Int, String>(2)
        cache.put(1, "a")
        cache.put(2, "b")
        cache.get(1)          // touch 1 -> 2 is now LRU
        cache.put(3, "c")     // evicts 2

        assertEquals("a", cache.get(1))
        assertNull(cache.get(2))
        assertEquals("c", cache.get(3))
    }

    @Test
    fun `put same key updates value`() {
        val cache = ThreadSafeLruCache<Int, String>(2)
        cache.put(1, "a")
        cache.put(1, "b")
        assertEquals("b", cache.get(1))
        assertEquals(1, cache.size)
    }

    @Test
    @Timeout(10)
    fun `concurrent access never exceeds capacity`() {
        val cache = ThreadSafeLruCache<Int, Int>(50)
        val threads = 8
        val opsPerThread = 2_000
        val start = CountDownLatch(1)
        val done = CountDownLatch(threads)

        val pool = Executors.newFixedThreadPool(threads)
        repeat(threads) { t ->
            pool.submit {
                start.await()
                repeat(opsPerThread) { i ->
                    val key = (t * opsPerThread + i) % 200
                    cache.put(key, i)
                    cache.get(key)
                    assertTrue(cache.size <= 50, "size exceeded capacity")
                }
                done.countDown()
            }
        }
        start.countDown()
        assertTrue(done.await(10, TimeUnit.SECONDS))
        pool.shutdownNow()
        assertTrue(cache.size <= 50)
    }
}
