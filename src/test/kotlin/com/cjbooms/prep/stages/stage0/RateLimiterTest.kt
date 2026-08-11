package com.cjbooms.prep.stages.stage0

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class RateLimiterTest {

    @Test
    fun `admits up to the limit then rejects`() {
        val limiter = RateLimiter(maxRequests = 3, perMillis = 1000)
        assertTrue(limiter.allow("r1", 0))
        assertTrue(limiter.allow("r2", 100))
        assertTrue(limiter.allow("r3", 200))
        assertFalse(limiter.allow("r4", 300))
    }

    @Test
    fun `admits again after the window slides`() {
        val limiter = RateLimiter(maxRequests = 2, perMillis = 1000)
        assertTrue(limiter.allow("r1", 0))
        assertTrue(limiter.allow("r2", 500))
        assertFalse(limiter.allow("r3", 600))
        // at t=1001, r1 (t=0) has expired -> one slot free
        assertTrue(limiter.allow("r4", 1001))
    }

    @Test
    fun `boundary - request exactly at expiry instant is admitted`() {
        val limiter = RateLimiter(maxRequests = 1, perMillis = 1000)
        assertTrue(limiter.allow("r1", 500))
        // half-open window [t-1000, t): t=1500 expires the t=500 entry
        assertTrue(limiter.allow("r2", 1500))
    }


    @Test
    fun `user admits up to the limit then rejects`() {
        val limiter = PerUserRateLimiter(maxRequests = 3, perMillis = 1000)
        assertTrue(limiter.allow("u1", "r1", 0))
        assertTrue(limiter.allow("u1", "r2", 100))
        assertTrue(limiter.allow("u1", "r3", 200))
        assertFalse(limiter.allow("u1", "r4", 300))
    }

    @Test
    fun `user admits again after the window slides`() {
        val limiter = PerUserRateLimiter(maxRequests = 2, perMillis = 1000)
        assertTrue(limiter.allow("u1", "r1", 0))
        assertTrue(limiter.allow("u1", "r2", 500))
        assertFalse(limiter.allow("u1", "r3", 600))
        // at t=1001, r1 (t=0) has expired -> one slot free
        assertTrue(limiter.allow("u1", "r4", 1001))
    }

    @Test
    fun `user boundary - request exactly at expiry instant is admitted`() {
        val limiter = PerUserRateLimiter(maxRequests = 1, perMillis = 1000)
        assertTrue(limiter.allow("u1", "r1", 500))
        // half-open window [t-1000, t): t=1500 expires the t=500 entry
        assertTrue(limiter.allow("u1", "r2", 1500))
    }


    @Test
    fun `bucket admits up to the limit then rejects`() {
        val limiter = TokenBucketRateLimiter(capacity = 3, refillPerMillis = .001)
        assertTrue(limiter.allow(0))
        assertTrue(limiter.allow(100))
        assertTrue(limiter.allow(200))
        assertFalse(limiter.allow(300))
    }

    @Test
    fun `bucket admits again after the window slides`() {
        val limiter = TokenBucketRateLimiter(capacity = 2, refillPerMillis = 0.001)
        assertTrue(limiter.allow(0))
        assertTrue(limiter.allow(10))
        assertFalse(limiter.allow(20))
        // at t=1001, r1 (t=0) has expired -> one slot free
        assertTrue(limiter.allow(1001))
    }

    @Test
    fun `bucket boundary - request exactly at expiry instant is admitted`() {
        val limiter = TokenBucketRateLimiter(capacity = 1, refillPerMillis = 1.0)
        assertTrue(limiter.allow(0))
        // half-open window [t-1000, t): t=1500 expires the t=500 entry
        assertTrue(limiter.allow(1))
    }
}
