package com.cjbooms.prep.stages.stage11

/**
 * Stage 11.1 — Kth largest element in a stream (15 min).
 *
 * MongoDB relevance: top-k-style queries against streaming oplog events,
 * finding the k most recent slow queries, leaderboards over append-only
 * event streams. A sorted structure keeps "what is the current kth largest?"
 * cheap as new elements arrive.
 *
 * Structure-selection ritual:
 *   - Full sort: O(n log n) per query — only OK if queries are rare.
 *   - Min-heap of size k: O(log k) per add, O(1) per peek at the kth largest.
 *     Invariant: the heap's top (smallest in the heap) is the current kth
 *     largest of everything seen so far. New value larger than the top -> swap
 *     in and re-heapify. New value smaller -> ignore (it can't enter the top k).
 *
 * Why not a max-heap of size n - k + 1? Either works; min-heap-of-size-k is
 * the canonical formulation and gives "peek in O(1)" for the answer.
 */
class KthLargest(private val k: Int) {

    init {
        require(k > 0) { "k must be positive, was $k" }
    }

    /**
     * Record a new value from the stream.
     *
     * Structure ritual: maintain a min-heap of size k. Drop the smallest
     * element from the heap whenever adding a value that exceeds it; that
     * keeps the heap = the current top k values, smallest of them on top.
     */
    fun add(value: Int) {
        TODO("implement")
    }

    /**
     * The current kth largest value among everything added so far.
     *
     * If fewer than k values have been added, this returns the smallest
     * value seen so far (the "kth largest" in a pool of fewer than k items).
     * The heap is therefore never empty unless no values have been added.
     */
    fun peek(): Int {
        TODO("implement")
    }

    private val minHeap: java.util.PriorityQueue<Int> = java.util.PriorityQueue()
}
