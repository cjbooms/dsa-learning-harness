package com.cjbooms.prep.stages.stage15

/**
 * Stage 15.1 — Cold-recall rebuild: fixed sliding-window rate limiter.
 *
 * MongoDB framing: Atlas Control Plane rate-limits incoming requests per tenant
 * to protect shared services. Same shape as the request governor you'd see in
 * a control-plane edge service.
 *
 * allow(requestId, nowMillis):
 *   - returns true if the request is admitted, false if rate-limited
 *   - at most [maxRequests] requests per trailing window of [perMillis] ms
 *   - requestIds are unique per attempt (use them to defend against double-add)
 *   - nowMillis is non-decreasing within a single instance
 *
 * Ritual before coding (speak aloud):
 *   1. Name two candidate structures (deque of timestamps? counter + tree map?).
 *   2. Pick one and say WHY in one sentence.
 *   3. THEN code.
 *
 * Time budget: 8 minutes cold.
 */
class RateLimiterRecall(private val maxRequests: Int, private val perMillis: Long) {


    fun allow(requestId: String, nowMillis: Long): Boolean {
        TODO("implement")
    }
}
