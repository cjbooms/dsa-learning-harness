package com.cjbooms.prep.realworld

import java.util.TreeMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Classic real-world problem: data-migration lag verifier.
 *
 * Events fire when a record leaves the primary and when it arrives at the
 * secondary. Raise an alert if any record's replication lag exceeds maxLagSeconds.
 *
 * Data structures (two indexes over the same set of in-flight records):
 *
 *   leaveTimeByRecordId: HashMap<recordId, leaveTimestamp>
 *     Exists so an ARRIVAL can find its leave event in O(1) — pair the events,
 *     compute the lag, remove the record from both indexes.
 *
 *   recordIdByLeaveTime: TreeMap<leaveTimestamp, recordId>
 *     The same records, sorted oldest-first by leave time. Exists so poll()
 *     can ask "everything older than the cutoff" directly — headMap(cutoff)
 *     IS the answer set, no scanning, no skipping.
 *
 * Arrival REMOVES the record from both indexes immediately (TreeMap removal
 * is O(log n)), so there is no deferred-cleanup bookkeeping anywhere.
 *
 * THREAD SAFETY: one ReentrantLock guards ALL state. The two indexes plus the
 * alerted set form a single composite invariant (a record is in both indexes
 * or neither), so thread-safe individual collections would NOT be enough —
 * e.g. ConcurrentHashMap + ConcurrentSkipListMap would still allow an arrival
 * to remove from one index while poll() reads the other mid-update.
 * Composite invariant -> single lock around every method.
 *
 * Messy-event handling (interviewers always probe this):
 *   - Arrival for a record we never saw leave: ignored (returns null).
 *   - Events out of order: pairing is by recordId, not arrival sequence.
 *   - Duplicate alert prevention: `alerted` remembers every recordId ever
 *     raised; a record alerts at the first poll that notices the breach, once.
 */
class ReplicationLagAlerter(private val maxLagSeconds: Long) {

    private val lock = ReentrantLock()

    // recordId -> when it left the primary. Every record not yet arrived,
    // INCLUDING ones already alerted (so late arrivals still pair up).
    private val leaveTimeByRecordId = HashMap<String, Long>()

    // Same records, keyed by leave time so range queries by age are direct.
    // (Assumes distinct leave timestamps; if two records can share a timestamp,
    //  make the value a set of recordIds — say this caveat aloud in the interview.)
    private val recordIdByLeaveTime = TreeMap<Long, String>()

    // Records we've already raised an alert for — exactly-once alerting.
    private val alerted = HashSet<String>()

    fun onPrimaryLeave(recordId: String, timestampSeconds: Long) = lock.withLock {
        leaveTimeByRecordId[recordId] = timestampSeconds
        recordIdByLeaveTime[timestampSeconds] = recordId
    }

    /** Pairs the arrival with its leave event and takes the record out of
     *  both indexes. Returns the observed lag, or null if we have no matching
     *  leave (missed/lost event). */
    fun onSecondaryArrive(recordId: String, timestampSeconds: Long): Long? = lock.withLock {
        val leaveTimestamp = leaveTimeByRecordId.remove(recordId) ?: return@withLock null
        recordIdByLeaveTime.remove(leaveTimestamp)
        timestampSeconds - leaveTimestamp
    }

    /** All records that have now been in flight longer than maxLagSeconds.
     *  Each breaching record appears in exactly one poll, ever. */
    fun poll(nowSeconds: Long): List<String> = lock.withLock {
        // Anything that left at or before this moment has been in flight
        // longer than the allowed lag.
        val breachCutoff = nowSeconds - maxLagSeconds

        // headMap(cutoff) = every in-flight record with leaveTime <= cutoff,
        // already sorted oldest-first. These are exactly the breaches.
        val breachedRecords = recordIdByLeaveTime.headMap(breachCutoff, true)

        val breachingRecordIds = mutableListOf<String>()
        for ((_, recordId) in breachedRecords) {
            if (alerted.add(recordId)) {
                breachingRecordIds.add(recordId)
            }
        }

        // Alerted records leave the AGE index so future polls don't rescan
        // them — but they stay in leaveTimeByRecordId, so a late arrival can
        // still be paired and its lag reported.
        breachedRecords.clear()

        breachingRecordIds
    }

    val inFlightCount: Int
        get() = lock.withLock { leaveTimeByRecordId.size }
}
