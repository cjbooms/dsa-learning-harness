package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SnapshotArrayTest {

    @Test
    fun `get before any set returns zero`() {
        val arr = SnapshotArray(3)
        assertEquals(0, arr.snap())
        assertEquals(0, arr.get(0, 0))
    }

    @Test
    fun `set and snap preserve history per index`() {
        val arr = SnapshotArray(3)
        arr.set(0, 5)
        val snap0 = arr.snap() // 0
        arr.set(0, 7)
        arr.set(1, 10)
        val snap1 = arr.snap() // 1
        arr.set(0, 9)

        assertEquals(0, snap0)
        assertEquals(1, snap1)

        assertEquals(5, arr.get(0, snap0))
        assertEquals(7, arr.get(0, snap1))
        assertEquals(9, arr.get(0, snap1 + 1))
        assertEquals(0, arr.get(1, snap0))
        assertEquals(10, arr.get(1, snap1))
        assertEquals(0, arr.get(2, snap1 + 1))
    }

    @Test
    fun `multiple snaps without set keep last value`() {
        val arr = SnapshotArray(1)
        arr.set(0, 4)
        val s0 = arr.snap()
        val s1 = arr.snap()
        val s2 = arr.snap()

        assertEquals(4, arr.get(0, s0))
        assertEquals(4, arr.get(0, s1))
        assertEquals(4, arr.get(0, s2))
    }
}
