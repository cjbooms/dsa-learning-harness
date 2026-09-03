package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class KthLargestTest {

    @Test
    fun `streaming kth largest tracks the running top k`() {
        // k = 3; after each add, peek is the 3rd largest seen so far.
        val kl = KthLargest(3)
        kl.add(3); assertEquals(3, kl.peek())
        kl.add(5); assertEquals(3, kl.peek())
        kl.add(10); assertEquals(3, kl.peek())
        kl.add(9); assertEquals(5, kl.peek())   // top-3: {5, 9, 10} -> 5
        kl.add(4); assertEquals(5, kl.peek())   // top-3 unchanged: {5, 9, 10}
        kl.add(11); assertEquals(9, kl.peek())  // top-3: {9, 10, 11} -> 9
    }

    @Test
    fun `k equals 1 returns the maximum seen so far`() {
        // k = 1 means the heap always holds the single biggest value seen.
        val kl = KthLargest(1)
        kl.add(7); assertEquals(7, kl.peek())
        kl.add(2); assertEquals(7, kl.peek())
        kl.add(11); assertEquals(11, kl.peek())
        kl.add(5); assertEquals(11, kl.peek())
    }

    @Test
    fun `peek returns smallest seen so far when fewer than k elements`() {
        // k = 2 but only one element added: the "2nd largest" is the only value.
        val kl = KthLargest(2)
        kl.add(42)
        assertEquals(42, kl.peek())
        kl.add(10)
        assertEquals(10, kl.peek()) // top-2 of {10, 42} -> 2nd largest is 10
    }

    @Test
    fun `k must be positive`() {
        assertThrows(IllegalArgumentException::class.java) {
            KthLargest(0)
        }
    }
}
