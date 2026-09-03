package com.cjbooms.prep.stages.stage12

/**
 * Stage 12.2 — Semaphore-based connection pool.
 * Time budget: 15 min.
 *
 * MongoDB context: the driver opens a bounded pool of TCP sockets to each
 * server (mongoc's maxPoolSize / Kotlin driver's maxConnections). The pool
 * MUST hand out at most N concurrent connections, and requests for more
 * must block (or time out) rather than exhaust the server's file
 * descriptors. A Semaphore with N permits is the textbook primitive for
 * "no more than N holders of this resource".
 *
 * Structure-selection ritual (say aloud before coding):
 *   1. Resource count   -> Semaphore(maxConnections) holds the permits.
 *   2. Acquire/release  -> lease() acquires a permit (blocks); release()
 *                          returns it. Pair them — every acquire needs a
 *                          release on every code path, including exceptions.
 *   3. Owned objects    -> keep a Deque<Connection> of idle connections so
 *                          a second caller doesn't pay connection setup twice.
 *   4. Why not just a lock? A lock caps to ONE holder; a semaphore caps to
 *      N. With a lock the pool would be a 1-connection pool. Wrong tool.
 *   5. Why not a blocking queue? A bounded queue caps the number of WAITERS
 *      sleeping, not the number of ACTIVE connections. Different problem.
 *
 * The lease()/release() pair is the testable contract. Pool warm-up,
 * health checks, and max-idle-time are all follow-ups.
 *
 * Interview follow-ups:
 *   - "What happens if release() is called twice for the same lease?"
 *     -> Semaphore.release() is unconditional; double-release grows permits
 *        past the bound. Track outstanding leases explicitly if you need
 *        to defend against it.
 *   - "Add a timeout?" -> lease(timeout, unit) returning null on failure.
 *   - "How do you evict an unhealthy connection?" -> caller marks it bad,
 *     pool drops it instead of returning it on release().
 */
class ConnectionPool(
    private val maxConnections: Int,
    private val openConnection: () -> Connection,
) {

    init {
        require(maxConnections > 0) { "maxConnections must be positive, was $maxConnections" }
    }

    private val permits = java.util.concurrent.Semaphore(maxConnections)

    private val idle = java.util.concurrent.ConcurrentLinkedDeque<Connection>()

    /**
     * Acquire a connection from the pool, blocking until one is available.
     * Caller MUST invoke [release] exactly once per successful lease,
     * even on exception paths.
     */
    fun lease(): Connection {
        TODO("implement")
    }

    fun release(conn: Connection) {
        TODO("implement")
    }

    /** Number of connections currently checked out (approximate, for tests). */
    val activeCount: Int
        get() {
            TODO("implement")
        }

    /**
     * Minimal Connection type for the exercise. In a real driver this is
     * a TCP socket; here it's a value-typed placeholder so tests can
     * construct it cheaply.
     */
    class Connection(val id: Int)
}
