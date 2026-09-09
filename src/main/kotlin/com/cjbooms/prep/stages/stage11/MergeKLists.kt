package com.cjbooms.prep.stages.stage11

import java.util.PriorityQueue

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
    if (lists.isEmpty()) return emptyList()
    val minHeap = PriorityQueue<ListNode>(lists.size, compareBy { it.value } )
    val results = mutableListOf<Int>()

    lists.forEach {
        if (it != null) minHeap.add(it)
    }
    while (minHeap.isNotEmpty()) {
        val node = minHeap.poll()
        results.add(node.value)
        if (node.next != null) minHeap.add(node.next)
    }
    return results
}

fun main() {
    val one = ListNode(1)
    val three = ListNode(3)
    one.next = three
    val two = ListNode(2)
    val four = ListNode(4)
    two.next = four

    println("Expected 1,2,3,4 Actual: " + mergeKLists(listOf(one, two)))

}