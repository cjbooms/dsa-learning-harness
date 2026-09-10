package com.cjbooms.prep.solutions.stage13

/**
 * Learn first: see docs/learning-resources.md
 * Time-based hit counter over a fixed sliding window of seconds.
 *
 * Records hit events at integer-second timestamps and answers queries for
 * "how many hits occurred in the last `windowSeconds` seconds". A hit at
 * time `t` counts for a query at time `now` iff `now - windowSeconds < t <= now`
 * (the window is half-open on the left: a hit exactly `windowSeconds` in
 * the past is not counted; a hit at the current timestamp is).
 *
 * @property windowSeconds width of the sliding window in seconds. Must be
 *   positive.
 */
class HitCounter(private val windowSeconds: Int = 300) {

    init {
        require(windowSeconds > 0) { "windowSeconds must be positive, was $windowSeconds" }
    }

    // each slot covers one second; second s lands at slot (s mod windowSeconds).
    private data class Bucket(var timestamp: Int, var count: Int)

    private val buckets = arrayOfNulls<Bucket>(windowSeconds)

    /**
     * Records a hit at wall-clock time [timestampSeconds].
     */
    fun hit(timestampSeconds: Int) {
        val slotIndex = indexOf(timestampSeconds, windowSeconds)
        val bucket = buckets[slotIndex]
        if (bucket == null || bucket.timestamp != timestampSeconds) {
            // stale slot: was last written for an older second; reset it.
            buckets[slotIndex] = Bucket(timestampSeconds, 1)
        } else {
            bucket.count += 1
        }
    }

    /**
     * Returns the number of hits in the half-open window
     * `(timestampSeconds - windowSeconds, timestampSeconds]`.
     */
    fun getHits(timestampSeconds: Int): Int {
        // half-open window: exclude hits at exactly `cutoff`.
        val cutoff = timestampSeconds - windowSeconds
        var total = 0
        for (slot in 0 until windowSeconds) {
            val bucket = buckets[slot] ?: continue
            if (bucket.timestamp > cutoff && bucket.timestamp <= timestampSeconds) {
                total += bucket.count
            }
        }
        return total
    }

    // kotlin's `%` follows the dividend's sign; we always want a non-negative index.
    private fun indexOf(value: Int, size: Int): Int {
        val remainder = value % size
        return if (remainder < 0) remainder + size else remainder
    }
}

fun main() {
    data class Test(val case: String, val expected: Int, val actual: Int) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // hits at 1, 2, 5 with window=5: at t=5 all three are inside (0, 5].
    val cut = HitCounter(windowSeconds = 5)
    cut.hit(1)
    cut.hit(2)
    cut.hit(5)
    Test("all three hits inside (0, 5]", 3, cut.getHits(5))
    Test("hit at current timestamp is included", 3, cut.getHits(5))

    // half-open on the left: hit at exactly `cutoff` (= now - window) is excluded.
    val boundary = HitCounter(windowSeconds = 5)
    boundary.hit(1)
    Test("hit exactly windowSeconds ago excluded by half-open window", 0, boundary.getHits(6))
    Test("same exclusion holds one second later", 0, boundary.getHits(7))

    // a hit just inside the window still counts.
    boundary.hit(2)
    Test("hit just inside the window is included", 1, boundary.getHits(6))

    // multiple hits in the same second all bucket together.
    val many = HitCounter(windowSeconds = 60)
    repeat(5) { many.hit(10) }
    many.hit(20)
    Test("multiple hits in the same second bucket together", 6, many.getHits(25))
    Test("all hits fall outside the window", 0, many.getHits(120))

    val empty = HitCounter(windowSeconds = 60)
    Test("empty counter reports zero", 0, empty.getHits(1000))
}
