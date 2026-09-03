package com.cjbooms.prep.stages.stage13

import java.util.Random

/**
 * Stage 13.5 — Randomized set: insert, remove, getRandom in O(1) (20 min).
 *
 * Why this matters for MongoDB: random sampling from a dynamic set of replica
 * nodes, random query-plan hints, A/B test bucket assignment, and reservoir-style
 * estimators all need uniform random access that survives insert/remove.
 *
 * Structure-selection ritual:
 *   - ArrayList alone gives O(1) getRandom but O(n) insert/remove.
 *   - HashMap alone gives O(1) insert/remove but no O(1) random element.
 *   - COMBINE: ArrayList stores the values (random index is O(1));
 *     HashMap<V, Int> stores value -> index, making removal O(1) via
 *     swap-with-last-then-pop.
 *
 * The swap trick is the whole question: overwrite the doomed element with the
 * tail element, update the tail's index in the map, then pop.
 */
class RandomizedSet<V>(private val random: Random = Random()) {

    /**
     * Adds [value] if not present. Returns true if inserted, false if already present. O(1).
     */
    fun insert(value: V): Boolean {
        TODO("implement")
    }

    /**
     * Removes [value] if present. Returns true if removed, false if absent. O(1).
     */
    fun remove(value: V): Boolean {
        TODO("implement")
    }

    /**
     * Returns a uniformly random element. O(1). Throws if the set is empty.
     */
    fun getRandom(): V {
        TODO("implement")
    }

    /** Current element count. O(1). */
    val size: Int
        get() {
            TODO("implement")
        }
}
