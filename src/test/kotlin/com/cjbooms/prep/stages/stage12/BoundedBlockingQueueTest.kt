package com.cjbooms.prep.stages.stage12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Tests for the producer/consumer bounded blocking queue.
 *
 * These tests are written against the EXPECTED contract of the skeleton.
 * They currently fail (the TODO stubs throw) and must pass once the
 * skeleton is implemented. The failures prove the test wiring is real,
 * not a no-op assert.
 */
class BoundedBlockingQueueTest {

    @Test
    @Timeout(10)
    fun `fifo ordering single threaded`() {
        val q = BoundedBlockingQueue<Int>(capacity = 3)
        q.put(1)
        q.put(2)
        q.put(3)
        assertEquals(1, q.take())
        assertEquals(2, q.take())
        assertEquals(3, q.take())
    }

    @Test
    @Timeout(10)
    fun `size tracks the number of buffered items`() {
        val q = BoundedBlockingQueue<String>(capacity = 4)
        assertEquals(0, q.size)
        q.put("a")
        q.put("b")
        assertEquals(2, q.size)
        q.take()
        assertEquals(1, q.size)
        q.take()
        assertEquals(0, q.size)
    }

    @Test
    @Timeout(10)
    fun `put blocks when full until take frees space`() {
        val q = BoundedBlockingQueue<Int>(capacity = 1)
        q.put(42)

        val secondPutReturned = AtomicInteger(0)
        val producerStarted = CountDownLatch(1)
        val executor = Executors.newSingleThreadExecutor()

        val producer = executor.submit {
            producerStarted.countDown()
            q.put(99)               // MUST block: capacity is 1, slot taken
            secondPutReturned.set(1)
        }

        // Give the producer time to enter put() and block.
        assertTrue(producerStarted.await(2, TimeUnit.SECONDS))
        Thread.sleep(200)
        assertEquals(0, secondPutReturned.get(), "producer should still be blocked")

        // Drain the queue: the blocked producer must now unblock and complete.
        assertEquals(42, q.take())
        producer.get(3, TimeUnit.SECONDS) // surfaces any deadlock as a TimeoutException
        assertEquals(1, secondPutReturned.get())
        assertEquals(99, q.take())

        executor.shutdownNow()
    }

    @Test
    @Timeout(10)
    fun `take blocks when empty until put adds an item`() {
        val q = BoundedBlockingQueue<String>(capacity = 2)

        val took = AtomicInteger(0)
        val started = CountDownLatch(1)
        val executor = Executors.newSingleThreadExecutor()

        val consumer = executor.submit {
            started.countDown()
            q.take()                  // MUST block: queue is empty
            took.set(1)
        }

        assertTrue(started.await(2, TimeUnit.SECONDS))
        Thread.sleep(200)
        assertEquals(0, took.get(), "consumer should still be blocked")

        q.put("hello")
        consumer.get(3, TimeUnit.SECONDS)
        assertEquals(1, took.get())

        executor.shutdownNow()
    }

    @Test
    @Timeout(15)
    fun `concurrent producers and consumers deliver every item exactly once`() {
        val q = BoundedBlockingQueue<Int>(capacity = 8)
        val producers = 4
        val consumers = 4
        val perProducer = 250
        val totalItems = producers * perProducer

        val seen = AtomicInteger(0)
        val consumed = java.util.concurrent.ConcurrentHashMap.newKeySet<Int>()
        val start = CountDownLatch(1)
        val producersDone = CountDownLatch(producers)
        val consumersDone = CountDownLatch(consumers)

        val pool = Executors.newFixedThreadPool(producers + consumers)

        repeat(producers) { p ->
            pool.submit {
                start.await()
                repeat(perProducer) { i ->
                    q.put(p * perProducer + i)
                }
                producersDone.countDown()
            }
        }

        repeat(consumers) {
            pool.submit {
                start.await()
                repeat(perProducer) {
                    val item = q.take()
                    consumed.add(item)
                    seen.incrementAndGet()
                }
                consumersDone.countDown()
            }
        }

        start.countDown()
        assertTrue(producersDone.await(10, TimeUnit.SECONDS), "producers did not finish")
        assertTrue(consumersDone.await(10, TimeUnit.SECONDS), "consumers did not finish")
        pool.shutdownNow()

        assertEquals(totalItems, seen.get(), "every produced item must be consumed")
        assertEquals(totalItems, consumed.size, "every item must be unique")
    }
}
