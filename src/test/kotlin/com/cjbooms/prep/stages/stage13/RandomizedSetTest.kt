package com.cjbooms.prep.stages.stage13

import java.util.Random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

class RandomizedSetTest {

    @Test
    fun `insert adds new values and rejects duplicates`() {
        val set = RandomizedSet<Int>()
        assertTrue(set.insert(1))
        assertTrue(set.insert(2))
        assertFalse(set.insert(1))
        assertEquals(2, set.size)
    }

    @Test
    fun `remove deletes present values and reports absent values`() {
        val set = RandomizedSet<String>()
        set.insert("a")
        set.insert("b")

        assertTrue(set.remove("a"))
        assertFalse(set.remove("a"))
        assertEquals(1, set.size)
        assertEquals("b", set.getRandom())
    }

    @Test
    fun `getRandom from empty set throws`() {
        assertThrows(IllegalStateException::class.java) {
            RandomizedSet<Int>().getRandom()
        }
    }

    @Test
    fun `getRandom is uniform over a seeded stream`() {
        val random = Random(12345L)
        val set = RandomizedSet<Int>(random)
        set.insert(10)
        set.insert(20)
        set.insert(30)

        val counts = mutableMapOf(10 to 0, 20 to 0, 30 to 0)
        repeat(10_000) {
            counts[set.getRandom()] = counts.getValue(set.getRandom()) + 1
        }

        counts.values.forEach { count ->
            assertTrue(count in 3000..3700, "expected roughly uniform counts, got $counts")
        }
    }
}
