package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class MergeKListsTest {

    /** Helper: build a linked list from a vararg of ints, return its head. */
    private fun linkedListOf(vararg values: Int): ListNode? {
        if (values.isEmpty()) return null
        val head = ListNode(values[0])
        var tail = head
        for (i in 1 until values.size) {
            tail.next = ListNode(values[i])
            tail = tail.next!!
        }
        return head
    }

    @Test
    fun `merges three sorted lists into one sorted sequence`() {
        val merged = mergeKLists(listOf(
            linkedListOf(1, 4, 7),
            linkedListOf(2, 5, 8),
            linkedListOf(3, 6, 9),
        ))
        assertEquals((1..9).toList(), merged)
    }

    @Test
    fun `handles duplicate values across lists deterministically`() {
        // 1 appears in two lists; output must still be sorted (1, 1, 2, 3).
        val merged = mergeKLists(listOf(
            linkedListOf(1, 3),
            linkedListOf(1, 2),
        ))
        assertEquals(listOf(1, 1, 2, 3), merged)
    }

    @Test
    fun `empty input yields empty output`() {
        assertTrue(mergeKLists(emptyList()).isEmpty())
    }

    @Test
    fun `null entries within the list are skipped`() {
        // One list is null (contributes nothing), the other two merge normally.
        val merged = mergeKLists(listOf(
            null,
            linkedListOf(1, 5),
            linkedListOf(2, 4),
        ))
        assertEquals(listOf(1, 2, 4, 5), merged)
    }

    @Test
    fun `single list passes through unchanged`() {
        val merged = mergeKLists(listOf(linkedListOf(2, 3, 7)))
        assertEquals(listOf(2, 3, 7), merged)
    }
}
