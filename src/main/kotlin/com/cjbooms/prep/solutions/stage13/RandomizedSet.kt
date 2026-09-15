package com.cjbooms.prep.solutions.stage13

import java.util.Random

/**
 * Stage 13.5 — Randomized set: insert, remove, getRandom in O(1) (20 min).
 *
 * Why this matters: random sampling from a dynamic set of replica
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

    private val values = ArrayList<V>()
    private val indexByValue = HashMap<V, Int>()

    fun insert(value: V): Boolean {
        if (indexByValue.containsKey(value)) return false
        indexByValue[value] = values.size
        values.add(value)
        return true
    }

    fun remove(value: V): Boolean {
        val index = indexByValue[value] ?: return false
        val lastIndex = values.size - 1
        if (index != lastIndex) {
            val lastValue = values[lastIndex]
            values[index] = lastValue
            indexByValue[lastValue] = index
        }
        values.removeAt(lastIndex)
        indexByValue.remove(value)
        return true
    }

    fun getRandom(): V {
        check(values.isNotEmpty()) { "cannot sample from an empty set" }
        return values[random.nextInt(values.size)]
    }

    val size: Int
        get() = values.size
}
