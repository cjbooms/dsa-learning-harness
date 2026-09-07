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

class UnionFind(n: Int) {

    val parent = IntArray(n) { it }
    val rank = IntArray(n) { 1 }

    fun print() {
        println("${parent.joinToString()}, ${rank.joinToString()}")
    }

    /**
     * Find the representative (root) of the component containing [x].
     *
     * @param x an element id in 0..n - 1.
     * @return the id of the representative of [x]'s component. Two elements
     *   share a representative if and only if they are in the same component.
     */
    fun find(x: Int): Int {
        if (parent[x] != x)
            parent[x] = find(parent[x])
        return parent[x]
    }

    /**
     * Merge the components containing [x] and [y].
     *
     * @param x an element id in 0..n - 1.
     * @param y an element id in 0..n - 1.
     * @return true if [x] and [y] were in different components and are now
     *   merged into one, or false if they were already in the same component.
     */
    fun union(x: Int, y: Int): Boolean {
        val parentX = find(x)
        val parentY = find(y)
        if (parentX == parentY) return false
        if (rank[parentX] == rank[parentY]) {
            rank[parentX]++
            parent[parentY] = parentX
        }
        else if (rank[parentX] < rank[parentY]) {
            parent[parentX] = parentY
        }
        else if (rank[parentY] < rank[parentX]) {
            parent[parentY] = parentX
        }
        return true
    }

    /**
     * Test whether [x] and [y] belong to the same component.
     *
     * @param x an element id in 0..n - 1.
     * @param y an element id in 0..n - 1.
     * @return true if [x] and [y] are in the same component, false otherwise.
     */
    fun connected(x: Int, y: Int): Boolean {
        return find(x) == find(y)
    }

    /**
     * Number of distinct components currently tracked.
     *
     * @return the count of components over the elements 0..n - 1.
     */
    fun componentCount(): Int {
        var count = 0
        parent.forEachIndexed { i, p ->
            if (i == p) count++
        }
        return count
    }
}

fun main() {
    val datastructure = UnionFind(10)
    datastructure.print()

    datastructure.union(0, 1) // Root 0 is now Rank 2
    datastructure.union(2, 3) // Root 2 is now Rank 2
    datastructure.union(4, 5) // Root 4 is now Rank 2
    datastructure.union(6, 7) // Root 6 is now Rank 2
    datastructure.print()

    // 2. Merge the Rank 2 trees to create two trees of Rank 3
    datastructure.union(0, 2) // Merges Root 0 and Root 2 -> Root 0 is now Rank 3
    datastructure.union(4, 6) // Merges Root 4 and Root 6 -> Root 4 is now Rank 3
    datastructure.print()

    // 3. Merge the Rank 3 trees to create a Rank 4 tree
    datastructure.union(0, 4) // Merges Root 0 and Root 4 -> Root 0 is now Rank
    datastructure.print()

}
