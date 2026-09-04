package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
 * Union-Find (Disjoint Set Union) over a fixed set of n elements.
 *
 * Maintains a partition of the elements 0..n - 1 into disjoint connected
 * components, supporting incremental edge insertion (union) and connectivity
 * queries (find/connected).
 *
 * @param n the number of elements, labelled 0..n - 1.
 */

/**
 * Find the representative (root) of the component containing [x].
 *
 * @param x an element id in 0..n - 1.
 * @return the id of the representative of [x]'s component. Two elements
 *   share a representative if and only if they are in the same component.
 */

/**
 * Merge the components containing [x] and [y].
 *
 * @param x an element id in 0..n - 1.
 * @param y an element id in 0..n - 1.
 * @return true if [x] and [y] were in different components and are now
 *   merged into one, or false if they were already in the same component.
 */

/**
 * Test whether [x] and [y] belong to the same component.
 *
 * @param x an element id in 0..n - 1.
 * @param y an element id in 0..n - 1.
 * @return true if [x] and [y] are in the same component, false otherwise.
 */

/**
 * Number of distinct components currently tracked.
 *
 * @return the count of components over the elements 0..n - 1.
 */
class UnionFind(n: Int) {

    fun find(x: Int): Int {
        TODO("implement")
    }

    fun union(x: Int, y: Int): Boolean {
        TODO("implement")
    }

    fun connected(x: Int, y: Int): Boolean {
        TODO("implement")
    }

    fun componentCount(): Int {
        TODO("implement")
    }
}
