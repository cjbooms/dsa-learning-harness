package com.cjbooms.prep.stages.stage8

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
