package com.cjbooms.prep.stages.stage3

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

    fun insert(docId: String, text: String) {
        TODO()
    }

    fun search(term: String): Set<String> {
        TODO()
    }

    fun searchAll(vararg terms: String): Set<String> {
        TODO()
    }

    fun delete(docId: String) {
        TODO()
    }
}
