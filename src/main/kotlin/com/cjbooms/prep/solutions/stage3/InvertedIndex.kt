package com.cjbooms.prep.solutions.stage3

/**
 * Stage 3.4 — Inverted index (REPORTED, Blind 2025 — Atlas Search team).
 *
 * insert(docId, text): index the document's terms (split on whitespace,
 * lowercase — keep tokenization simple and say so).
 * search(term): docIds containing the term.
 *
 * Reported follow-ups (implement after the basics work):
 *   - searchAll("a", "b"): docs containing ALL terms (AND query)
 *   - delete(docId): remove a document completely
 *
 * Structure ritual: the map direction is the whole question — what maps to
 * what, and what does delete cost in your chosen direction?
 */
class InvertedIndex {

    // term -> set of docIds containing that term
    private val index = mutableMapOf<String, MutableSet<String>>()
    // docId -> terms it contributed, so delete is O(terms in doc)
    private val termsByDoc = mutableMapOf<String, MutableSet<String>>()

    fun insert(docId: String, text: String) {
        // Simple whitespace/lowercase tokenization; say this aloud in interview.
        val terms = text.split(Regex("\\s+")).filter { it.isNotEmpty() }.map { it.lowercase() }.toSet()
        termsByDoc[docId] = terms.toMutableSet()
        for (term in terms) {
            index.getOrPut(term) { mutableSetOf() }.add(docId)
        }
    }

    fun search(term: String): Set<String> {
        return index[term.lowercase()]?.toSet() ?: emptySet()
    }

    fun searchAll(vararg terms: String): Set<String> {
        if (terms.isEmpty()) return emptySet()
        val normalized = terms.map { it.lowercase() }
        val first = index[normalized[0]] ?: return emptySet()
        var result = first.toSet()
        for (i in 1 until normalized.size) {
            val set = index[normalized[i]] ?: return emptySet()
            result = result.intersect(set)
            if (result.isEmpty()) return emptySet()
        }
        return result
    }

    fun delete(docId: String) {
        val terms = termsByDoc.remove(docId) ?: return
        for (term in terms) {
            index[term]?.remove(docId)
        }
    }
}
