package com.cjbooms.prep.stages.stage15

/**
 * A fixed sliding-window rate limiter that admits at most [maxRequests] calls
 * within any trailing window of [perMillis] milliseconds.
 *
 * @param maxRequests the maximum number of requests permitted in any window
 * @param perMillis the size of the trailing window in milliseconds
 */
class RateLimiterRecall(private val maxRequests: Int, private val perMillis: Long) {

    /**
     * Decides whether a request should be admitted under the rate limit.
     *
     * @param requestId a unique identifier for this request attempt
     * @param nowMillis the current time in milliseconds; non-decreasing across
     *   calls on the same instance
     * @return `true` if the request is admitted, `false` if it is rate-limited
     */
    fun allow(requestId: String, nowMillis: Long): Boolean {
        TODO("implement")
    }
}
