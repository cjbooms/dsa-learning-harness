package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
 * Redundant Connection.
 *
 * A valid tree is an undirected graph that is connected and has absolutely
 * no cycles. You are given a graph that started as a tree with n nodes
 * labeled from 1 to n. Exactly one additional edge was added to this graph,
 * creating a cycle.
 *
 * @param edges a 2D array of length n where edges[i] = [u, v] represents an
 *   undirected edge between nodes u and v. Nodes are labeled from 1 to n.
 * @return the exact edge that can be removed to eliminate the cycle, as an
 *   IntArray of [u, v]. If there are multiple answers, return the edge that
 *   occurs last in the input array.
 */
fun findRedundantConnection(edges: Array<IntArray>): IntArray {
    val length = edges.size
    val cycles = mutableSetOf<IntArray>()
    val unionFind = UnionFindInner(length)
    edges.forEach {
        if (unionFind.union(it[0] - 1, it[1] -1) == false) {
            println("Already in same component ${it.joinToString()}")
            cycles.add(it)
        }
    }

    println("All cycles ${cycles.joinToString{it.joinToString()}}")
    return cycles.last()
}

fun main() {

    println(
        "Expected 6, 4 Found: " +
        findRedundantConnection(
        arrayOf(
            intArrayOf(1,2),
            intArrayOf(2,3),
            intArrayOf(3,1),
            intArrayOf(4,5),
            intArrayOf(5,6),
            intArrayOf(6,4),
            )
    ).joinToString()
    )
}



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

class UnionFindInner(n: Int) {

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
        parent.forEach { it ->
            if (rank[it] == 1) count++
        }
        return count
    }
}