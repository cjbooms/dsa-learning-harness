package com.cjbooms.prep.stages.stage8

/**
 * Stage 8.8 — Grid traversal patterns: islands and rotting oranges (40 min).
 *
 * Why this matters for MongoDB: geospatial / image-grid analytics, chunk layout
 * reasoning (contiguous shard key ranges), and cluster-health propagation
 * ("how many minutes until a failure spreads to every affected node?").
 *
 * Structure-selection ritual:
 *   - For `countIslands`, every land cell starts a flood-fill if it hasn't been
 *     visited yet. Either DFS recursion or an explicit stack/queue works; the
 *     stub uses in-place mutation (sink visited '1's to '0') so no extra
 *     visited matrix is needed.
 *   - For `rottingOranges`, single-source BFS (see [ShortestPath.kt]) is the
 *     wrong shape: rot spreads from EVERY initially rotten orange simultaneously.
 *     Seed the queue with all rotten cells, then process level by level. Each
 *     level is one elapsed minute.
 *
 * Time budget: 40 min. Defend aloud: why multi-source BFS for spreading-state
 * problems but single-source BFS for point-to-point shortest path?
 */
fun countIslands(grid: Array<CharArray>): Int {
    TODO("implement")
}

fun rottingOranges(grid: Array<IntArray>): Int {
    TODO("implement")
}
