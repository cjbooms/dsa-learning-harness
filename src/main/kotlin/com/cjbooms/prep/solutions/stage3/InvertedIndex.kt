package com.cjbooms.prep.solutions.stage3

/**
 * Stage 3.4 — Inverted index (common full-text search exercise).
 *
 * Why this matters: full-text search is built on top of an inverted index under
 * the hood; this exercise is the in-memory version of "term -> docs".
 *
 * Structure-selection ritual:
 *   - The map direction is the whole question — what maps to what, and what
 *     does delete cost in your chosen direction?
 *   - Forward-only (term -> docIds): search is O(1), but delete has to scan
 *     every term in every doc to find which postboxes still mention docId.
 *   - Two-way (term -> docIds AND docId -> terms): delete is O(terms in doc)
 *     because we already know which terms to evict from the forward map.
 *     That's the trade we take here.
 */
class InvertedIndex {

    // term -> docIds that contain it.
    val index = hashMapOf<String, MutableSet<String>>()
    // reverse map so delete is O(terms in doc).
    val termsByDoc = hashMapOf<String, MutableSet<String>>()

    fun insert(docId: String, text: String) {
        // Simple whitespace/lowercase tokenization; note this aloud.
        val rawTerms = text.split(Regex("\\s+"))
        val terms = hashSetOf<String>()
        for (raw in rawTerms) {
            if (raw.isEmpty()) continue
            terms.add(raw.lowercase())
        }
        // On re-insert, evict docId from every old term's bucket first — otherwise
        // stale docIds stay in the forward map and break search.
        val previousTerms = termsByDoc.remove(docId)
        if (previousTerms != null) {
            for (term in previousTerms) {
                val bucket = index[term] ?: continue
                bucket.remove(docId)
                if (bucket.isEmpty()) index.remove(term)
            }
        }
        termsByDoc[docId] = terms
        for (term in terms) {
            val bucket = index.getOrPut(term) { mutableSetOf() }
            bucket.add(docId)
        }
    }

    fun search(term: String): Set<String> {
        return index[term.lowercase()]?.toSet() ?: emptySet()
    }

    fun searchAll(vararg terms: String): Set<String> {
        if (terms.isEmpty()) return emptySet()
        val first = index[terms[0].lowercase()] ?: return emptySet()
        var result = first.toSet()
        for (termIndex in 1 until terms.size) {
            val bucket = index[terms[termIndex].lowercase()] ?: return emptySet()
            result = result intersect bucket
            if (result.isEmpty()) return emptySet()
        }
        return result
    }

    fun delete(docId: String) {
        val terms = termsByDoc.remove(docId) ?: return
        for (term in terms) {
            val bucket = index[term] ?: continue
            bucket.remove(docId)
            if (bucket.isEmpty()) index.remove(term)
        }
    }
}

fun main() {
    data class Test(val case: String, val expected: Set<String>, val actual: Set<String>) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    val idx = InvertedIndex()

    // Happy path: a term appearing in two docs.
    idx.insert("doc1", "The quick brown fox")
    idx.insert("doc2", "The lazy dog")
    Test(
        case = "Single term search across two docs",
        expected = setOf("doc1", "doc2"),
        actual = idx.search("the"),
    )
    Test(
        case = "Term only in one doc",
        expected = setOf("doc1"),
        actual = idx.search("fox"),
    )
    Test(
        case = "Missing term returns empty set",
        expected = emptySet(),
        actual = idx.search("nonexistent"),
    )

    // AND query.
    Test(
        case = "AND query intersecting two terms",
        expected = setOf("doc1"),
        actual = idx.searchAll("the", "quick"),
    )
    Test(
        case = "AND query with no overlap returns empty",
        expected = emptySet(),
        actual = idx.searchAll("fox", "dog"),
    )

    // Insert replaces: stale terms must lose the docId too.
    idx.insert("doc1", "Renamed completely")
    Test(
        case = "Re-insert removes doc from stale term buckets",
        expected = emptySet(),
        actual = idx.search("quick"),
    )
    Test(
        case = "Re-insert leaves other docs alone",
        expected = setOf("doc2"),
        actual = idx.search("the"),
    )
    Test(
        case = "New terms visible after re-insert",
        expected = setOf("doc1"),
        actual = idx.search("renamed"),
    )

    // Delete: every term the doc contributed must be evicted.
    idx.delete("doc2")
    Test(
        case = "After delete, doc2 is gone from every postbox",
        expected = emptySet(),
        actual = idx.search("the"),
    )
    Test(
        case = "Deleted doc no longer matches",
        expected = emptySet(),
        actual = idx.search("dog"),
    )
    Test(
        case = "Other doc unaffected by delete",
        expected = setOf("doc1"),
        actual = idx.search("renamed"),
    )
    Test(
        case = "Deleting absent docId is a no-op",
        expected = emptySet(),
        actual = idx.search("anything"),
    )
}
