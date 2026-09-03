package com.cjbooms.prep.stages.stage12

/**
 * Stage 12.2 — Multithreaded web crawler (15 min).
 *
 * MongoDB relevance: crawling metadata indexes, changestream topology
 * discovery, and any graph where edges are discovered lazily by many
 * workers. The challenge is combining BFS reachability with safe shared
 * mutable state.
 *
 * Structure-selection ritual:
 *   - A plain BFS queue + visited set works, but one thread cannot saturate
 *     I/O wait. Use a fixed worker pool.
 *   - Shared state: a work queue, a visited set, and an in-flight counter.
 *   - Mark a URL visited when you DEQUEUE it; enqueue links freely and skip
 *     duplicates on dequeue. Pre-marking before enqueue prevents fetching.
 *   - Termination: stop when the queue is empty AND no worker is currently
 *     fetching.
 *
 * Time budget: 15 min.
 */
class WebCrawler(
    private val fetcher: (String) -> List<String>,
    private val threadCount: Int = 4,
) {

    /**
     * Starts at [startUrl], fetches pages concurrently, and returns the set of
     * all reachable URLs. The fetcher returns the outgoing links for a URL.
     */
    fun crawl(startUrl: String): List<String> {
        TODO("implement")
    }
}
