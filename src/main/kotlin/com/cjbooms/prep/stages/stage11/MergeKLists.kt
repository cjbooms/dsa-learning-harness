package com.cjbooms.prep.stages.stage11

/**
 * Stage 11.2 — Merge K sorted lists via min-heap (15 min).
 *
 * MongoDB relevance: ordered merge of K already-sorted index ranges, sorted
 * scan over sharded merge-sort output, time-ordered union of per-shard cursors.
 * Compare with the lazy iterator approach in Stage 3.2 — heap wins on
 * asymptotic per-element cost for large k (O(log k) vs O(k)), at the price
 * of holding one element per list in memory.
 *
 * Structure-selection ritual:
 *   - Map<index, currentHead>: O(k) per emit — fine for small k.
 *   - Min-heap of size k: O(log k) per emit — the canonical "k-way merge".
 *   Heap entry carries (value, listIndex, elementIndex) so equal values from
 *   different lists are ordered consistently (deterministic output).
 */
class ListNode(var value: Int) {
    var next: ListNode? = null
}

/**
 * Merge k sorted linked lists and return the sorted values.
 *
 *   mergeKLists(listOf(1->4->7, 2->5->8, 3->6->9)) == [1..9]
 *
 * Empty / null lists in the input are skipped (they contribute nothing).
 * Empty overall input returns an empty list.
 */
fun mergeKLists(lists: List<ListNode?>): List<Int> {
    TODO("implement")
}
