package com.cjbooms.prep.stages.stage0

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Conventional per-user sliding-window limiter — the shape commonly
 * expected, for benchmarking against PerUserRateLimiter.
 *
 * Design: ONE structure per user, no global index.
 *   windows: HashMap<userId, ArrayDeque<timestampMillis>>
 *
 * Each user's deque holds only that user's in-window timestamps, oldest at
 * the front (insertion order == time order, since nowMillis is
 * non-decreasing). Expiry is per-user and lazy: we only ever clean the
 * deque of the user making THIS request.
 *
 * Why this is the conventional answer:
 *   - every operation on the hot path is O(1) amortized:
 *     peek front / pop front / append / size — all deque O(1)
 *   - no cross-user bookkeeping at all on admit/reject
 *   - the redundant requestId->time map disappears: requestIds are unique,
 *     so the deque can hold timestamps directly
 *
 * Memory bounding (rung 0.4) is deliberately NOT on the hot path here:
 * evictIdleUsers() is a separate sweep the caller runs periodically
 * (scheduled task). Alternative discussed: a
 * TreeMap<lastActivity, userId> secondary index for O(log n) eviction —
 * at the cost of maintaining two structures in sync on every call.
 */
class PerUserRateLimiterConventional(
    private val maxRequests: Int,
    private val perMillis: Long,
) {
    private val windows = HashMap<String, ArrayDeque<Long>>()
    private val lock = ReentrantLock()

    fun allow(userId: String, nowMillis: Long): Boolean {
        lock.withLock {
            val window = windows.getOrPut(userId) { ArrayDeque() }
            val expiryCutoff = nowMillis - perMillis

            // Lazy per-user expiry: timestamps older than the window leave.
            // Each entry leaves once -> amortized O(1) per allow() call.
            while (true) {
                val oldest = window.firstOrNull() ?: break
                if (oldest > expiryCutoff) break // front is newest-enough; rest are newer
                window.removeFirstOrNull()
            }

            if (window.size >= maxRequests) return false

            window.addLast(nowMillis)
            return true
        }
    }

    /** Rung 0.4: periodic sweep, NOT per-call. O(users) — fine on a schedule. */
    fun evictIdleUsers(nowMillis: Long) {
        lock.withLock {
            val expiryCutoff = nowMillis - perMillis
            val iterator = windows.entries.iterator()
            while (iterator.hasNext()) {
                val (userId, window) = iterator.next()
                while (true) {
                    val oldest = window.firstOrNull() ?: break
                    if (oldest > expiryCutoff) break
                    window.removeFirstOrNull()
                }
                if (window.isEmpty()) iterator.remove()
            }
        }
    }

    // Visible for benchmarking.
    val userCount: Int get() = lock.withLock { windows.size }
}
