package com.cjbooms.prep.concurrency

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class BoundedBlockingQueueTest {

    @Test
    @Timeout(10)
    fun `fifo ordering single threaded`() {
        val q = BoundedBlockingQueue<Int>(3)
        q.put(1); q.put(2); q.put(3)
        assertEquals(1, q.take())
        assertEquals(2, q.take())
        assertEquals(3, q.take())
    }

    @Test
    @Timeout(10)
    fun `all items flow through with concurrent producers and consumers`() {
        val q = BoundedBlockingQueue<Int>(4)
        val producers = 4
        val consumers = 4
        val perProducer = 250
        val total = producers * perProducer
        val consumed = AtomicInteger(0)
        val sum = AtomicInteger(0)
        val done = CountDownLatch(consumers)

        val pool = Executors.newFixedThreadPool(producers + consumers)
        repeat(producers) { p ->
            pool.submit {
                for (i in 1..perProducer) q.put(p * perProducer + i)
            }
        }
        repeat(consumers) {
            pool.submit {
                while (consumed.incrementAndGet() <= total) {
                    sum.addAndGet(q.take())
                }
                done.countDown()
            }
        }

        assertTrue(done.await(10, TimeUnit.SECONDS))
        pool.shutdownNow()
        val expected = (0 until producers).sumOf { p -> (1..perProducer).sumOf { p * perProducer + it } }
        assertEquals(expected, sum.get())
    }

    @Test
    @Timeout(10)
    fun `put blocks when full until take frees space`() {
        val q = BoundedBlockingQueue<Int>(1)
        q.put(1)
        val putReturned = CountDownLatch(1)
        val consumerStarted = CountDownLatch(1)

        val pool = Executors.newFixedThreadPool(2)
        pool.submit {
            q.put(2) // must block until consumer takes 1
            putReturned.countDown()
        }
        pool.submit {
            consumerStarted.countDown()
            assertEquals(1, q.take())
        }

        consumerStarted.await(5, TimeUnit.SECONDS)
        // give the producer a chance to (incorrectly) complete early
        assertTrue(putReturned.await(5, TimeUnit.SECONDS))
        assertEquals(2, q.take())
        pool.shutdownNow()
    }

    @Test
    @Timeout(10)
    fun `synchronized variant passes same smoke test`() {
        val q = SynchronizedBoundedBlockingQueue<Int>(2)
        q.put(1); q.put(2)
        assertEquals(1, q.take())
        q.put(3)
        assertEquals(2, q.take())
        assertEquals(3, q.take())
    }
}
