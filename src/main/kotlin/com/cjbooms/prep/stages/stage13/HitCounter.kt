package com.cjbooms.prep.stages.stage13

/**
 * Stage 13.2 — Time-based hit counter (LeetCode 362 / design).
 *
 * Why this matters for MongoDB: per-shard QPS metering, rate-limit windows
 * the cheap way, slow-query dashboard "events in the last N seconds",
 * replication lag histograms, Oplog window auditing. Compare with the
 * sliding-log rate limiter in Stage 0 — same data shape, different
 * eviction strategy.
 *
 * Structure-selection ritual:
 *   1. Naive: keep every hit timestamp in a deque. O(hits) memory.
 *   2. Fixed-window buckets (one counter per second) — O(window) memory,
 *      clean O(1) per hit.
 *   3. Circular buffer: array of size [windowSeconds], rotate index by
 *      `now % windowSeconds`. Each slot holds the count for that bucket
 *      AND the timestamp of the most recent hit that landed in it; on
 *      arrival, if the slot's timestamp != now, reset it before adding.
 *   4. For per-event timestamps (not bucketed), keep a deque of (ts, count)
 *      pairs and binary-search the cutoff — useful when the question is
 *      "count hits in last k arbitrary millis, not just whole seconds".
 *
 * Time budget: 20 minutes. See stage doc 13.
 *
 * Implementation: circular-buffer buckets (option 3) — O(1) per hit,
 * O(windowSeconds) per query. Window semantics: hits `t` count iff
 * `now - windowSeconds < t <= now` (half-open on the LEFT: a hit exactly
 * `windowSeconds` in the past is NOT counted; a hit at the current
 * timestamp IS counted). Each bucket stores `(timestamp, count)` for the
 * second it represents; a hit into a stale bucket (different timestamp)
 * resets it before incrementing.
 */
class HitCounter(private val windowSeconds: Int = 300) {

    init {
        require(windowSeconds > 0) { "windowSeconds must be positive, was $windowSeconds" }
    }

    /**
     * Records a hit at wall-clock time [timestampSeconds]. O(1) amortized.
     */
    fun hit(timestampSeconds: Int) {
        TODO("implement")
    }

    /**
     * Returns the number of hits in the half-open window
     * `(timestampSeconds - windowSeconds, timestampSeconds]`.
     * O(windowSeconds) is acceptable.
     */
    fun getHits(timestampSeconds: Int): Int {
        TODO("implement")
    }
}
