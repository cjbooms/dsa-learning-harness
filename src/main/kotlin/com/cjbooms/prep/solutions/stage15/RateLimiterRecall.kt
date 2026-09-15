package com.cjbooms.prep.solutions.stage15

/**
 * Stage 15.1 — Cold-recall rebuild: fixed sliding-window rate limiter.
 *
 * Real-world framing: cloud control plane rate-limits incoming requests per tenant
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

    // Sliding window via an ArrayDeque of (requestId, timestamp).
    // O(1) amortized per call: evict from the head while entries are older than
    // (nowMillis - perMillis), then admit at the tail if size < maxRequests.
    // The timestamp doubles as the ordering key, so no separate map is needed.
    val inflightRequests = ArrayDeque<Pair<String, Long>>(maxRequests)

    fun allow(requestId: String, nowMillis: Long): Boolean {
        // Evict expired entries from the head of the window.
        while (true) {
            val oldest = inflightRequests.firstOrNull() ?: break
            if ((nowMillis - oldest.second) >= perMillis) {
                inflightRequests.removeFirstOrNull()
            } else break
        }
        return if (inflightRequests.size < maxRequests) {
            inflightRequests.addLast(requestId to nowMillis)
            true
        } else false
    }
}
