package com.cjbooms.prep.stages.stage0

/**
 * Stage 0.1 — Rebuild your screen solution from memory.
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
 * Ritual before coding: name 2 candidate structures, defend your pick aloud.
 * (You solved this with a deque + map in the screen — can you rebuild it cold?)
 *
 * Rung follow-ups (same file, new classes — see stage doc 00):
 *   0.2 thread-safe      -> ConcurrentRateLimiter
 *   0.3 per-user limits  -> PerUserRateLimiter
 *   0.4 memory bounding  -> eviction inside PerUserRateLimiter
 *   0.5 token bucket     -> TokenBucketRateLimiter (API + refill math only)
 */
class RateLimiter(private val maxRequests: Int, private val perMillis: Long) {

    fun allow(requestId: String, nowMillis: Long): Boolean {
        TODO("Rebuild your screen solution: drop expired, check window, admit")
    }
}

/**
 * 0.2 — same semantics, safe under concurrent calls.
 * Which tool: lock, semaphore, or atomics? Narrate the choice.
 */
class ConcurrentRateLimiter(private val maxRequests: Int, private val perMillis: Long) {

    fun allow(requestId: String, nowMillis: Long): Boolean {
        TODO("Guard the window state. What is the minimal critical section?")
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
    fun allow(userId: String, requestId: String, nowMillis: Long): Boolean {
        TODO("Per-user window. And: when do idle users get evicted?")
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
    fun allow(nowMillis: Long): Boolean {
        TODO("tokens = min(capacity, tokens + elapsed * refillPerMillis); spend 1 if >= 1")
    }
}
