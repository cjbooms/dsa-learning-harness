package com.cjbooms.prep.stages.stage3

/**
 * Learn first: see docs/learning-resources.md
 * An in-memory inverted index mapping terms to the documents that contain
 * them.
 *
 * `insert(docId, text)` adds the document's terms to the index. `text` is
 * split on whitespace and lowercased before indexing. `search(term)` returns
 * the set of document IDs containing the given term. `searchAll(vararg terms)`
 * returns the set of document IDs containing every supplied term. `delete`
 * removes a document from the index.
 */
class InvertedIndex {

    /**
     * Inserts the terms of [text] under the document identifier [docId],
     * replacing any prior contents for [docId]. Terms are split on whitespace
     * and lowercased.
     *
     * @param docId the document identifier
     * @param text the document text to index
     */
    fun insert(docId: String, text: String) {
        TODO()
    }

    /**
     * Returns the set of document IDs whose indexed text contains [term].
     *
     * @param term the term to look up
     * @return the set of matching document IDs (empty if none)
     */
    fun search(term: String): Set<String> {
        TODO()
    }

    /**
     * Returns the set of document IDs whose indexed text contains every term
     * in [terms].
     *
     * @param terms the terms to look up; documents must contain all of them
     * @return the set of matching document IDs (empty if none)
     */
    fun searchAll(vararg terms: String): Set<String> {
        TODO()
    }

    /**
     * Removes [docId] and all of its terms from the index.
     *
     * @param docId the document identifier to remove
     */
    fun delete(docId: String) {
        TODO()
    }
}
