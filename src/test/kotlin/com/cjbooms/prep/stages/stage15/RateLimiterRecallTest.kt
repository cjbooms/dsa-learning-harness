package com.cjbooms.prep.stages.stage15

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class RateLimiterRecallTest {

    @Test
    fun `admits up to the limit then rejects`() {
        val limiter = RateLimiterRecall(maxRequests = 3, perMillis = 1000)
        assertTrue(limiter.allow("r1", 0))
        assertTrue(limiter.allow("r2", 100))
        assertTrue(limiter.allow("r3", 200))
        assertFalse(limiter.allow("r4", 300))
    }

    @Test
    fun `admits again after the window slides`() {
        val limiter = RateLimiterRecall(maxRequests = 2, perMillis = 1000)
        assertTrue(limiter.allow("r1", 0))
        assertTrue(limiter.allow("r2", 500))
        assertFalse(limiter.allow("r3", 600))
        // at t=1001 the t=0 entry has expired -> one slot free
        assertTrue(limiter.allow("r4", 1001))
    }

    @Test
    fun `boundary - request exactly at expiry instant is admitted`() {
        val limiter = RateLimiterRecall(maxRequests = 1, perMillis = 1000)
        assertTrue(limiter.allow("r1", 500))
        // half-open window [t-1000, t): t=1500 expires the t=500 entry
        assertTrue(limiter.allow("r2", 1500))
    }

    @Test
    fun `eviction keeps the in-flight set bounded`() {
        val limiter = RateLimiterRecall(maxRequests = 2, perMillis = 100)
        // Fire many requests far apart — none should be rejected, because the
        // older entries must be evicted as the window slides.
        for (i in 0 until 100) {
            assertTrue(limiter.allow("r$i", i * 1000L))
        }
    }
}
