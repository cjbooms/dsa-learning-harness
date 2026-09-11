package com.cjbooms.prep.stages.stage3

import java.util.Locale
import java.util.TreeMap

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

    val docIdToText = hashMapOf<String, MutableSet<String>>()
    val termToDocIds = hashMapOf<String, MutableSet<String>>()

    /**
     * Inserts the terms of [text] under the document identifier [docId],
     * replacing any prior contents for [docId]. Terms are split on whitespace
     * and lowercased.
     *
     * @param docId the document identifier
     * @param text the document text to index
     */
    fun insert(docId: String, text: String) {
        if (docIdToText.containsKey(docId)) {
            // UPDATE CASE = Must clean up
            val oldSearchKeys = docIdToText[docId]!!
            oldSearchKeys.forEach {
                termToDocIds[it]?.remove(docId)
            }
        }
        val searchKeys = text.toSearchKeys()
        docIdToText[docId] = searchKeys // Insert or Update
        searchKeys.forEach { searchTerm ->
            termToDocIds.getOrPut(searchTerm) { mutableSetOf() }.add(docId)
        }
    }


    fun String.toSearchKeys(): MutableSet<String> {
        return this.split(' ').map { it.toSearchKey() }.toMutableSet()
    }

    fun String.toSearchKey() = trim().lowercase(Locale.US)

    /**
     * Returns the set of document IDs whose indexed text contains [term].
     *
     * @param term the term to look up
     * @return the set of matching document IDs (empty if none)
     */
    fun search(term: String): Set<String> {
        return termToDocIds[term.toSearchKey()] ?: emptySet()
    }

    /**
     * Returns the set of document IDs whose indexed text contains every term
     * in [terms].
     *
     * @param terms the terms to look up; documents must contain all of them
     * @return the set of matching document IDs (empty if none)
     */
    fun searchAll(vararg terms: String): Set<String> {
        val searchKeys = terms.toSet().map { it.toSearchKey() }

        var matchDocIds: Set<String> = mutableSetOf()
        searchKeys.forEach { searchKey ->
            val resultsOfKey = termToDocIds[searchKey]
            if (resultsOfKey.isNullOrEmpty()) return emptySet()

            if (matchDocIds.isEmpty()) {
                // Must be first time
                matchDocIds = resultsOfKey
            }
            matchDocIds = matchDocIds.intersect(termToDocIds[searchKey] ?: emptySet())
        }


        return matchDocIds
    }

    /**
     * Removes [docId] and all of its terms from the index.
     *
     * @param docId the document identifier to remove
     */
    fun delete(docId: String) {
        if (!docIdToText.containsKey(docId)) return

        val searchKeys = docIdToText[docId]!!
        searchKeys.forEach {
            termToDocIds[it]?.remove(docId)
        }
        docIdToText.remove(docId)
    }
}

fun main() {
    data class Test(val case: String, val expected: Set<String>, val actual: Set<String>) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    val classUnderTest = InvertedIndex()
    Test(
        case = "Empty Map",
        expected = emptySet(),
        actual = classUnderTest.search("Not Found")
    )

    classUnderTest.insert("found-id", "found")
    Test(
        case = "Single Item, Single search key",
        expected = setOf("found-id"),
        actual = classUnderTest.search("Found")
    )

    classUnderTest.insert("multi-terms", "Lot of Search Terms")
    Test(
        case = "Find by middle word",
        expected = setOf("multi-terms"),
        actual = classUnderTest.search("of")
    )
    Test(
        case = "Find by tail word",
        expected = setOf("multi-terms"),
        actual = classUnderTest.search("Lot")
    )
    Test(
        case = "Find by head word",
        expected = setOf("multi-terms"),
        actual = classUnderTest.search("terms")
    )
    Test(
        case = "Do not find by white space",
        expected = emptySet(),
        actual = classUnderTest.search(" ")
    )

    // Multiple Docs Test
    classUnderTest.insert("second-multi-terms", "Lot of Different Words")
    Test(
        case = "Find two matches with middle word",
        expected = setOf("multi-terms", "second-multi-terms"),
        actual = classUnderTest.search("of")
    )
    Test(
        case = "Find two match with head word",
        expected = setOf("multi-terms", "second-multi-terms"),
        actual = classUnderTest.search("Lot")
    )
    Test(
        case = "Find one by head tail",
        expected = setOf("multi-terms"),
        actual = classUnderTest.search("terms")
    )
    Test(
        case = "Do not find by white space",
        expected = emptySet(),
        actual = classUnderTest.search(" ")
    )

    // Search All
    Test(
        case = "All words must return only match",
        expected = setOf("second-multi-terms"),
        actual = classUnderTest.searchAll("Lot", "of", "Different", "Words")
    )
    Test(
        case = "All words must return only match",
        expected = setOf("multi-terms", "second-multi-terms"),
        actual = classUnderTest.searchAll("Lot", "of")
    )

    // DELETE Removes Items
    classUnderTest.delete("second-multi-terms")
    Test(
        case = "Single Item, Single search key",
        expected = setOf("multi-terms"),
        actual = classUnderTest.search("Lot")
    )
}
