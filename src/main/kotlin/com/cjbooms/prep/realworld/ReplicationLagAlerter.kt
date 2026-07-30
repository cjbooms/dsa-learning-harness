package com.cjbooms.prep.realworld

/**
 * Reported MongoDB "real-world problem" round (2026): data-migration lag verifier.
 *
 * Events fire when a record leaves the primary and when it arrives at the
 * secondary. Raise an alert if any record's replication lag exceeds [maxLagSeconds].
 *
 * Model:
 *  - onPrimaryLeave(recordId, timestampSeconds)
 *  - onSecondaryArrive(recordId, timestampSeconds)
 *  - poll(nowSeconds): returns recordIds that left more than maxLagSeconds ago
 *    and have not yet arrived (and have not already been alerted).
 *
 * Approach: HashMap<recordId, leaveTs> for in-flight records + a min-heap ordered
 * by leaveTs so poll() only inspects records old enough to possibly breach.
 * Events may arrive out of order; an arrival for an unknown record is tolerated
 * (e.g., leave event was missed) and ignored.
 */
class ReplicationLagAlerter(private val maxLagSeconds: Long) {

    private data class InFlight(val recordId: String, val leaveTs: Long)

    private val inFlight = HashMap<String, Long>()
    private val byAge = java.util.PriorityQueue<InFlight>(compareBy { it.leaveTs })
    private val alerted = HashSet<String>()

    fun onPrimaryLeave(recordId: String, timestampSeconds: Long) {
        inFlight[recordId] = timestampSeconds
        byAge.add(InFlight(recordId, timestampSeconds))
    }

    fun onSecondaryArrive(recordId: String, timestampSeconds: Long): Long? {
        val leaveTs = inFlight.remove(recordId) ?: return null
        return timestampSeconds - leaveTs // observed lag; heap entry left as lazy tombstone
    }

    /** Records in flight longer than maxLagSeconds (alerted once each). */
    fun poll(nowSeconds: Long): List<String> {
        val alerts = mutableListOf<String>()
        while (true) {
            val head = byAge.peek() ?: break
            if (nowSeconds - head.leaveTs <= maxLagSeconds) break
            byAge.poll()
            // stale heap entries (already arrived) are silently dropped
            if (inFlight[head.recordId] == head.leaveTs && alerted.add(head.recordId)) {
                alerts.add(head.recordId)
            }
        }
        return alerts
    }

    val inFlightCount: Int get() = inFlight.size
}
