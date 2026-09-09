package com.cjbooms.prep.stages.stage13

import java.util.Random

/**
 * Learn first: see docs/learning-resources.md
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


    private val set = mutableListOf<V>()
    private val index = hashMapOf<V, Int>()

    /**
     * Adds [value] if not present. Returns `true` if inserted, `false` if
     * already present.
     */
    fun insert(value: V): Boolean {
        if (index.containsKey(value)) return false
        set.addLast(value)
        index[value] = set.size - 1
        return true
    }

    /**
     * Removes [value] if present. Returns `true` if removed, `false` if
     * absent.
     */
    fun remove(value: V): Boolean {
        if (!index.containsKey(value)) return false
        val currentIndex = index[value]!!
        index.remove(value)
        val last = set.removeLast()
        if (last != value) {
            set[currentIndex] = last
            index[last] = currentIndex
        }
        return true
    }

    /**
     * Returns a uniformly random element from the set.
     *
     * @throws NoSuchElementException if the set is empty.
     */
    fun getRandom(): V {
        if (set.isEmpty()) throw NoSuchElementException()
        val i = random.nextInt(set.size)
        return set[i]
    }

    /** Current element count. */
    val size: Int
        get() {
            return set.size
        }
}
