package com.cjbooms.prep.stages.stage13

/**
 * Learn first: see docs/learning-resources.md
 * Time-based hit counter over a fixed sliding window of seconds.
 *
 * Records hit events at integer-second timestamps and answers queries for
 * "how many hits occurred in the last `windowSeconds` seconds". A hit at
 * time `t` counts for a query at time `now` iff `now - windowSeconds < t <= now`
 * (the window is half-open on the left: a hit exactly `windowSeconds` in the
 * past is not counted; a hit at the current timestamp is).
 *
 * @property windowSeconds width of the sliding window in seconds. Must be
 *   positive.
 */
class HitCounter(private val windowSeconds: Int = 300) {

    init {
        require(windowSeconds > 0) { "windowSeconds must be positive, was $windowSeconds" }
    }

    /**
     * Records a hit at wall-clock time [timestampSeconds].
     */
    fun hit(timestampSeconds: Int) {
        TODO("implement")
    }

    /**
     * Returns the number of hits in the half-open window
     * `(timestampSeconds - windowSeconds, timestampSeconds]`.
     */
    fun getHits(timestampSeconds: Int): Int {
        TODO("implement")
    }
}
