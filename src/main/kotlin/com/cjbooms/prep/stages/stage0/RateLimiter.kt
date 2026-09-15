package com.cjbooms.prep.stages.stage0

import java.util.Deque
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.min


/**
 * Stage 0.1 — Rebuild the solution from memory.
 *
 * Fixed sliding-window rate limiter: at most [maxRequests] requests allowed
 * in any trailing window of [perMillis] milliseconds.
 *
 * allow(requestId, nowMillis):
 *   - returns true if the request is admitted, false if rate-limited
 *   - requestIds are unique per request attempt
 *   - nowMillis is non-decreasing within a single limiter instance
 *   - target: O(1) amortized per call
 *
 * Ritual before coding: name 2 candidate structures, defend your pick out loud.
 * (You solved this with a deque + map in the initial exercise — can you rebuild it cold?)
 *
 * Rung follow-ups (same file, new classes — see stage doc 00):
 *   0.2 thread-safe      -> ConcurrentRateLimiter
 *   0.3 per-user limits  -> PerUserRateLimiter
 *   0.4 memory bounding  -> eviction inside PerUserRateLimiter
 *   0.5 token bucket     -> TokenBucketRateLimiter (API + refill math only)
 */
class RateLimiter(private val maxRequests: Int, private val perMillis: Long) {

    //val timeOfRequest = HashMap<String, Long>()
    val inflightRequests = ArrayDeque<Pair<String, Long>>(maxRequests)

    fun allow(requestId: String, nowMillis: Long): Boolean {
        var isInflightAccurate = false
        while (!isInflightAccurate) {
            val oldest = inflightRequests.firstOrNull()

            if (oldest != null && (nowMillis - oldest.second) >= perMillis) {
                inflightRequests.removeFirstOrNull()
            } else {
                isInflightAccurate = true
            }
        }
        return if (inflightRequests.size < maxRequests) {
            inflightRequests.add(requestId to nowMillis)
            true
        } else false
    }
}

/**
 * 0.2 — same semantics, safe under concurrent calls.
 * Which tool: lock, semaphore, or atomics? Narrate the choice.
 */
class ConcurrentRateLimiter(private val maxRequests: Int, private val perMillis: Long) {

    val inflightRequests = ArrayDeque<Pair<String, Long>>(maxRequests)
    val lock = ReentrantLock()

    fun allow(requestId: String, nowMillis: Long): Boolean {
        lock.withLock {
            var isInflightAccurate = false
            while (!isInflightAccurate) {
                val oldest = inflightRequests.firstOrNull()

                if (oldest != null && (nowMillis - oldest.second) >= perMillis) {
                    inflightRequests.removeFirstOrNull()
                } else {
                    isInflightAccurate = true
                }
            }
            return if (inflightRequests.size < maxRequests) {
                inflightRequests.add(requestId to nowMillis)
                true
            } else false
        }
    }
}

/**
 * 0.3 + 0.4 — per-user windows, with idle-user eviction for memory bounding.
 * Key question to answer in a comment: where do you store last-activity so
 * eviction is cheap? (You may already have it.)
 */
class PerUserRateLimiter(
    private val maxRequests: Int,
    private val perMillis: Long,
) {


    var nextCleanTime: Long? = null
    val cleanIntervalMillis = 10_000
    val userRequests = hashMapOf<String, ArrayDeque<Pair<String, Long>>>()
    val lock = ReentrantLock()

    fun allow(userId: String, requestId: String, nowMillis: Long): Boolean {
        lock.withLock {
            val userContext = userRequests.getOrPut(userId) { ArrayDeque(maxRequests) }
            var clean = false
            val expiredMillis = nowMillis - perMillis
            while (!clean) {
                if (userContext.isNotEmpty() && userContext.first().second <= expiredMillis) {
                    userContext.removeFirstOrNull()
                } else {
                    clean = true
                }
            }
            if (nextCleanTime == null) nextCleanTime = nowMillis + cleanIntervalMillis
            if (nowMillis >= nextCleanTime!!) {
                periodicCleanup(expiredMillis)
                nextCleanTime = nowMillis + cleanIntervalMillis
            }
            if (userContext.size >= maxRequests) {
                return false
            }
            else {
                userContext.add(Pair(requestId, nowMillis))
                return true
            }



        }
    }

    private fun periodicCleanup(expiredMillis: Long) {
        val userForRemoval = mutableSetOf<String>()
        userRequests.forEach { userId, requests ->
            requests.lastOrNull().let {
                if (it == null || it.second <= expiredMillis) {
                    userForRemoval.add(userId)
                }
            }
        }
        userForRemoval.forEach {
            userRequests.remove(it)
        }

    }

}

/**
 * 0.5 — token bucket, API + math only (no full implementation needed).
 * capacity: max burst; refillPerMillis: steady-state rate.
 * In comments: refill formula, and sliding-log vs token-bucket trade-offs.
 */
class TokenBucketRateLimiter(
    private val capacity: Int,
    private val refillPerMillis: Double,
) {

    var tokens = capacity.toDouble()
    val NOT_INITIALIZED = -1L
    var lastRefillTime = NOT_INITIALIZED

    val lock = ReentrantLock()

    fun allow(nowMillis: Long): Boolean {
        lock.withLock {
            val tokensIncrease = if (lastRefillTime != NOT_INITIALIZED) {
                (nowMillis - lastRefillTime) * refillPerMillis
            } else 0.0
            lastRefillTime = nowMillis

            tokens = min((tokens + tokensIncrease), capacity.toDouble())

            return if (tokens >= 1.0) {
                tokens -= 1
                true
            } else false
        }
    }
}
