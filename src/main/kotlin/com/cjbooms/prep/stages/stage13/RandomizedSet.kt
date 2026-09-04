package com.cjbooms.prep.stages.stage13

import java.util.Random

/**
 * Set that supports `insert`, `remove`, and uniform `getRandom` in O(1).
 *
 * Each value may appear at most once. `insert` of an existing value returns
 * `false` without changing the set; otherwise it returns `true`. `remove`
 * returns `true` if the value was present and removed, `false` otherwise.
 * `getRandom` returns a uniformly random element and throws if the set is
 * empty.
 *
 * @param V element type.
 * @param random random source used by `getRandom`.
 */
class RandomizedSet<V>(private val random: Random = Random()) {

    /**
     * Adds [value] if not present. Returns `true` if inserted, `false` if
     * already present.
     */
    fun insert(value: V): Boolean {
        TODO("implement")
    }

    /**
     * Removes [value] if present. Returns `true` if removed, `false` if
     * absent.
     */
    fun remove(value: V): Boolean {
        TODO("implement")
    }

    /**
     * Returns a uniformly random element from the set.
     *
     * @throws NoSuchElementException if the set is empty.
     */
    fun getRandom(): V {
        TODO("implement")
    }

    /** Current element count. */
    val size: Int
        get() {
            TODO("implement")
        }
}
