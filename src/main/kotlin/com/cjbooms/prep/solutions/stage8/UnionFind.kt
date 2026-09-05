package com.cjbooms.prep.solutions.stage8

/**
 * Stage 8.5 — Union-Find with path compression + union by rank.
 *
 * Why this matters for MongoDB: dynamic connectivity over clusters, network
 * partition detection, "are these two shards in the same replica set?". When
 * edges arrive one at a time and you re-query repeatedly, union-find beats
 * DFS/BFS — nearly O(1) amortised per op with both optimisations.
 *
 * Structure-selection ritual:
 *   - parent[i] = parent of i (eventually a root).
 *   - rank[i]   = approximate tree depth (used to choose the new root on
 *     union).
 *   - find(x): walk up to root; compress the path along the way (re-parent
 *     every visited node to the root).
 *   - union(x, y): link root-of-smaller-rank under root-of-larger-rank.
 *
 * Time budget: 15 min. Defend aloud: when is union-find actually worth the
 * machinery over plain DFS? (When edges arrive incrementally and you need
 * many "are these connected?" queries between additions.)
 */
class UnionFind(n: Int) {

    private val parent: IntArray = IntArray(n) { it }
    private val rank: IntArray = IntArray(n)
    private var count: Int = n

    fun find(node: Int): Int {
        var root = node
        while (parent[root] != root) root = parent[root]
        // Path compression: re-parent every visited node to the root.
        var current = node
        while (parent[current] != root) {
            val next = parent[current]
            parent[current] = root
            current = next
        }
        return root
    }

    fun union(nodeA: Int, nodeB: Int): Boolean {
        val rootA = find(nodeA)
        val rootB = find(nodeB)
        if (rootA == rootB) return false
        // Union by rank: attach the shallower tree under the deeper one.
        when {
            rank[rootA] < rank[rootB] -> parent[rootA] = rootB
            rank[rootA] > rank[rootB] -> parent[rootB] = rootA
            else -> {
                parent[rootB] = rootA
                rank[rootA]++
            }
        }
        count--
        return true
    }

    fun connected(nodeA: Int, nodeB: Int): Boolean = find(nodeA) == find(nodeB)

    fun componentCount(): Int = count
}
