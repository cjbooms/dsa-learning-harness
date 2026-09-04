package com.cjbooms.prep.stages.stage11

/**
 * Learn first: see docs/learning-resources.md
 * Merge k sorted linked lists and return a single sorted list of values.
 *
 * Each input list is a singly linked list whose `next` pointers form a
 * non-decreasing sequence of integers. Merge them into one sorted sequence
 * of values.
 *
 * Parameter:
 *  - [lists]: the input lists. Entries may be `null` or empty and are
 *    skipped.
 *
 * Returns:
 *  - the sorted list of values from all non-empty inputs. If every input is
 *    empty or `null`, the result is an empty list.
 *
 * Example:
 *  - `mergeKLists(listOf(1->4->7, 2->5->8, 3->6->9)) == [1, 2, 3, 4, 5, 6, 7, 8, 9]`
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
