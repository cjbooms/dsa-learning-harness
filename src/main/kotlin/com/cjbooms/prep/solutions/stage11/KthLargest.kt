package com.cjbooms.prep.solutions.stage11

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
        // Step 1: always admit the value. We can always decide to drop it
        // back out once we know whether it belongs in the top k.
        minHeap.add(value)
        // Step 2: if admitting pushed us past size k, evict the smallest —
        // that's the current k+1-th largest, which cannot be in the top k.
        if (minHeap.size > k) minHeap.poll()
    }

    /**
     * The current kth largest value among everything added so far.
     *
     * If fewer than k values have been added, this returns the smallest
     * value seen so far (the "kth largest" in a pool of fewer than k items).
     * The heap is therefore never empty unless no values have been added.
     */
    fun peek(): Int {
        check(minHeap.isNotEmpty()) { "peek called before any value was added" }
        // Min-heap root is the smallest of the tracked values. Once size == k
        // that value is the kth largest overall; with fewer items it is the
        // smallest seen so far, which is the correct running answer.
        return minHeap.peek()
    }

    private val minHeap: java.util.PriorityQueue<Int> = java.util.PriorityQueue()
}
