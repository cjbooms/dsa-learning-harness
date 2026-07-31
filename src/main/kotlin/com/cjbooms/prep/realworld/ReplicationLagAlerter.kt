package com.cjbooms.prep.realworld

import java.util.PriorityQueue

/**
 * Reported MongoDB "real-world problem" round (2026): data-migration lag verifier.
 *
 * Events fire when a record leaves the primary and when it arrives at the
 * secondary. Raise an alert if any record's replication lag exceeds maxLagSeconds.
 *
 * Data structures and why:
 *
 *   inFlight: HashMap<recordId, leaveTimestamp>
 *     Source of truth for "still waiting for this record". O(1) pairing when
 *     the arrival event shows up.
 *
 *   byAge: min-heap of (recordId, leaveTimestamp), ordered by leave time
 *     Lets poll() look ONLY at the oldest in-flight record: if the oldest
 *     hasn't breached the lag limit, no younger record has either. This turns
 *     poll() from "scan everything" into "pop until the head is young enough".
 *
 *   alerted: HashSet<recordId>
 *     Exactly-once alerting — a breaching record alerts at the first poll that
 *     notices it, and never again.
 *
 * Out-of-order / messy events (interviewers always probe this):
 *   - Arrival for a record we never saw leave: ignored (returns null).
 *   - Arrival BEFORE we process the (late) leave event: tolerated — pairing is
 *     by recordId, not by event order.
 *   - Stale heap entries: when a record arrives, its heap entry is left behind
 *     as a lazy tombstone (removing it from the heap would be O(n)); poll()
 *     detects tombstones by cross-checking inFlight and silently drops them.
 */
class ReplicationLagAlerter(private val maxLagSeconds: Long) {

    private data class InFlightRecord(val recordId: String, val leaveTimestamp: Long)

    // recordId -> when it left the primary. Only records still awaiting arrival.
    private val inFlight = HashMap<String, Long>()

    // Same records, ordered oldest-first so poll() can stop at the first
    // "young enough" entry. May contain stale entries for arrived records.
    private val byAge = PriorityQueue<InFlightRecord>(compareBy { it.leaveTimestamp })

    // Records we've already raised an alert for.
    private val alerted = HashSet<String>()

    fun onPrimaryLeave(recordId: String, timestampSeconds: Long) {
        inFlight[recordId] = timestampSeconds
        byAge.add(InFlightRecord(recordId, timestampSeconds))
    }

    /** Pairs the arrival with its leave event. Returns the observed lag,
     *  or null if we have no matching leave (missed/lost event). */
    fun onSecondaryArrive(recordId: String, timestampSeconds: Long): Long? {
        val leaveTimestamp = inFlight.remove(recordId) ?: return null
        // The heap entry for this record is now stale; poll() will skip it.
        return timestampSeconds - leaveTimestamp
    }

    /** All records that have now been in flight longer than maxLagSeconds.
     *  Each breaching record appears in exactly one poll, ever. */
    fun poll(nowSeconds: Long): List<String> {
        val breachingRecordIds = mutableListOf<String>()

        // Peek at the oldest entry; stop when it's young enough that nothing
        // breaches (heap order guarantees nothing younger breaches either).
        // peek() = look at the min WITHOUT removing it; null if heap is empty.
        var oldest = byAge.peek()
        while (oldest != null && (nowSeconds - oldest.leaveTimestamp) > maxLagSeconds) {
            // poll() = REMOVE and return the min. The entry is out of the heap;
            // `oldest` still references it for the checks below.
            byAge.poll()

            // Skip stale tombstones: entries left over from records that
            // already arrived (gone from inFlight) or were re-put newer.
            val isStillInFlight = inFlight[oldest.recordId] == oldest.leaveTimestamp
            if (isStillInFlight && alerted.add(oldest.recordId)) {
                breachingRecordIds.add(oldest.recordId)
            }

            oldest = byAge.peek()
        }

        return breachingRecordIds
    }

    val inFlightCount: Int
        get() = inFlight.size
}
