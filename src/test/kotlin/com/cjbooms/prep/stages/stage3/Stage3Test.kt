package com.cjbooms.prep.stages.stage3

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IteratorsTest {
    @Test
    fun `union of two sorted streams`() {
        val result = unionSorted(listOf(1, 3, 5).iterator(), listOf(1, 2, 3).iterator())
        assertEquals(listOf(1, 2, 3, 5), result.asSequence().toList())
    }

    @Test
    fun `merge k sorted streams`() {
        val result = mergeKSorted(listOf(
            listOf(1, 4, 7).iterator(),
            listOf(2, 5, 8).iterator(),
            listOf(3, 6, 9).iterator(),
        ))
        assertEquals((1..9).toList(), result.asSequence().toList())
    }

    @Test
    fun `merge k sorted streams with priority q`() {
        val result = mergeKSortedPriorityQ(listOf(
            listOf(1, 4, 7).iterator(),
            listOf(2, 5, 8).iterator(),
            listOf(3, 6, 9).iterator(),
        ))
        assertEquals((1..9).toList(), result.asSequence().toList())
    }
}

class JsonParserTest {
    @Test
    fun `scalars`() {
        assertEquals(JsonValue.JsonNumber(42.0), parse("42"))
        assertEquals(JsonValue.JsonString("hi"), parse("\"hi\""))
        assertEquals(JsonValue.JsonTrue, parse("true"))
        assertEquals(JsonValue.JsonNull, parse("null"))
    }

    @Test
    fun `nested object and array`() {
        val parsed = parse("""{"a": [1, true, null], "b": {"c": "x"}}""")
        val obj = parsed as JsonValue.JsonObject
        val arr = obj.entries["a"] as JsonValue.JsonArray
        assertEquals(3, arr.items.size)
        assertEquals(JsonValue.JsonString("x"), (obj.entries["b"] as JsonValue.JsonObject).entries["c"])
    }
}

class InvertedIndexTest {
    @Test
    fun `insert and search`() {
        val index = InvertedIndex()
        index.insert("d1", "the quick brown fox")
        index.insert("d2", "the lazy dog")
        assertEquals(setOf("d1", "d2"), index.search("the"))
        assertEquals(setOf("d1"), index.search("fox"))
    }

    @Test
    fun `AND query and delete`() {
        val index = InvertedIndex()
        index.insert("d1", "quick brown fox")
        index.insert("d2", "quick red fox")
        assertEquals(setOf("d2"), index.searchAll("quick", "red"))
        index.delete("d2")
        assertEquals(setOf("d1"), index.search("fox"))
    }
}
