package com.cjbooms.prep.stages.stage0

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Correctness parity + rough throughput comparison between the two per-user
 * implementations. Not a microbenchmark — just enough to feel the difference
 * in eviction cost shape.
 */
class PerUserBenchmarkTest {

    @Test
    fun `parity - both accept and reject identically on a shared trace`() {
        val yours = PerUserRateLimiter(maxRequests = 3, perMillis = 1000)
        val conventional = PerUserRateLimiterConventional(maxRequests = 3, perMillis = 1000)

        val users = listOf("alice", "bob", "carol")
        var tick = 0L
        repeat(3_000) { i ->
            val user = users[i % users.size]
            tick += 7 // ~143 req/s spread over 3 users -> pressure without flooding
            val expected = conventional.allow(user, tick)
            val actual = yours.allow(user, "req-$i", tick)
            assertEquals(expected, actual, "divergence at tick=$tick user=$user")
        }
    }

    @Test
    fun `throughput comparison`() {
        val users = 10_000
        val requests = 200_000

        val yoursMs = measure {
            val limiter = PerUserRateLimiter(maxRequests = 10, perMillis = 1000)
            var tick = 0L
            repeat(requests) { i ->
                tick += 1
                limiter.allow("user-${i % users}", "req-$i", tick)
            }
        }

        val conventionalMs = measure {
            val limiter = PerUserRateLimiterConventional(maxRequests = 10, perMillis = 1000)
            var tick = 0L
            repeat(requests) { i ->
                tick += 1
                limiter.allow("user-${i % users}", tick)
            }
        }

        println("yours:        ${yoursMs}ms")
        println("conventional: ${conventionalMs}ms")
        // Not an assertion on which wins — JIT and allocation patterns dominate
        // at this scale. The point is to LOOK at the shape of the difference.
        assertTrue(yoursMs >= 0 && conventionalMs >= 0)
    }

    private fun measure(block: () -> Unit): Long {
        block() // warmup
        val start = System.nanoTime()
        block()
        return (System.nanoTime() - start) / 1_000_000
    }
}
