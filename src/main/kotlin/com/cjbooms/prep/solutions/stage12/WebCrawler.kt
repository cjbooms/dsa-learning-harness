package com.cjbooms.prep.solutions.stage12

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

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
 *     All three need concurrent safety.
 *   - Mark a URL visited when you DEQUEUE it; enqueue links freely and skip
 *     duplicates on dequeue. Pre-marking before enqueue prevents fetching.
 *   - Termination: stop when the queue is empty AND no worker is currently
 *     fetching.
 *   - One ReentrantLock around the worker loop guards the "queue empty AND
 *     inFlight == 0" exit predicate, mirroring the BoundedBlockingQueue idiom
 *     (`while` around `await()`, `signal()` on each new URL).
 *
 * Time budget: 15 min.
 */
class WebCrawler(
    private val fetcher: (String) -> List<String>,
    private val threadCount: Int = 4,
) {

    // shared state for the worker pool — all accessed under `lock`.
    private val queue = ConcurrentLinkedQueue<String>()
    private val visited = ConcurrentHashMap.newKeySet<String>()
    private var inFlight = 0

    private val lock = ReentrantLock()
    private val workAvailable = lock.newCondition()
    private val workDone = lock.newCondition()

    /**
     * Start at [startUrl], fetch pages concurrently, and return every URL
     * reachable from it.
     */
    fun crawl(startUrl: String): List<String> {
        // seed the queue under the lock so the first worker doesn't miss it.
        lock.withLock {
            queue.offer(startUrl)
            workAvailable.signal()
        }

        val workers = (1..threadCount).map {
            thread { runWorker() }
        }
        workers.forEach { it.join() }

        return visited.toList().sorted()
    }

    private fun runWorker() {
        while (true) {
            val url = lock.withLock {
                // while, not if: await() returns spuriously and after another
                // worker grabbed the last URL between our wake and our re-acquire.
                while (queue.isEmpty() && inFlight > 0) {
                    workAvailable.await()
                }
                if (queue.isEmpty()) {
                    // queue empty AND no worker is fetching -> the crawl is over.
                    workDone.signal()
                    return@withLock null
                }
                queue.poll()
            } ?: return

            // dequeue-time visited marking: the same URL enqueued by multiple
            // parents is only fetched by the worker that wins the visited set.
            if (!visited.add(url)) continue

            lock.withLock { inFlight++ }
            try {
                val links = fetcher(url)
                lock.withLock {
                    for (link in links) {
                        queue.offer(link)
                    }
                    // one new URL enqueued (at most) — wake exactly one worker.
                    workAvailable.signal()
                }
            } finally {
                lock.withLock {
                    inFlight--
                    // if we were the last in-flight worker, wake the rest so
                    // they can observe `queue.isEmpty() && inFlight == 0`.
                    if (inFlight == 0) workDone.signalAll()
                }
            }
        }
    }
}

fun main() {
    data class Test(val case: String, val expected: List<String>, val actual: List<String>) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // tiny linear chain a -> b -> c
    val chain = mapOf(
        "a" to listOf("b"),
        "b" to listOf("c"),
        "c" to emptyList<String>(),
    )
    Test(
        case = "Linear chain crawls all reachable URLs",
        expected = listOf("a", "b", "c"),
        actual = WebCrawler({ chain[it] ?: emptyList() }).crawl("a"),
    )

    // diamond: start -> {x, y}, both x and y point to z
    val diamond = mapOf(
        "start" to listOf("x", "y"),
        "x" to listOf("z"),
        "y" to listOf("z"),
        "z" to emptyList<String>(),
    )
    Test(
        case = "Diamond deduplicates shared downstream URL",
        expected = listOf("start", "x", "y", "z"),
        actual = WebCrawler({ diamond[it] ?: emptyList() }, threadCount = 2).crawl("start"),
    )

    // cycle with branch: a -> {b, c}, b -> a, c -> a
    val cycle = mapOf(
        "a" to listOf("b", "c"),
        "b" to listOf("a"),
        "c" to listOf("a"),
    )
    Test(
        case = "Cycle terminates without infinite loop",
        expected = listOf("a", "b", "c"),
        actual = WebCrawler({ cycle[it] ?: emptyList() }, threadCount = 3).crawl("a"),
    )

    // lonely start, no outgoing links
    Test(
        case = "Single URL with no edges crawls only the start",
        expected = listOf("only"),
        actual = WebCrawler({ emptyList<String>() }).crawl("only"),
    )
}
