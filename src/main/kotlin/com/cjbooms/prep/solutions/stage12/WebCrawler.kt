package com.cjbooms.prep.solutions.stage12

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

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
 *   - visited must be updated atomically BEFORE enqueue so the same URL is
 *     never fetched twice, even under races.
 *   - Termination: stop when the queue is empty AND no worker is currently
 *     fetching.
 *
 * Time budget: 15 min.
 */
class WebCrawler(
    private val fetcher: (String) -> List<String>,
    private val threadCount: Int = 4,
) {

    fun crawl(startUrl: String): List<String> {
        val queue = ConcurrentLinkedQueue<String>()
        val visited = ConcurrentHashMap.newKeySet<String>()
        val inFlight = AtomicInteger(0)

        queue.offer(startUrl)

        val executor = Executors.newFixedThreadPool(threadCount)
        val workers = (1..threadCount).map {
            executor.submit {
                while (true) {
                    val url = queue.poll()
                    if (url == null) {
                        if (inFlight.get() == 0) break
                        continue
                    }
                    // Mark visited at poll time so the same URL is never
                    // fetched twice, even if it was enqueued by multiple parents.
                    if (!visited.add(url)) continue

                    inFlight.incrementAndGet()
                    try {
                        val links = fetcher(url)
                        for (link in links) {
                            queue.offer(link)
                        }
                    } finally {
                        inFlight.decrementAndGet()
                    }
                }
            }
        }

        workers.forEach { it.get() }
        executor.shutdownNow()

        return visited.toList().sorted()
    }
}
