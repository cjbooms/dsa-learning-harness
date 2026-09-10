package com.cjbooms.prep.solutions.stage14

/**
 * Learn first: see docs/learning-resources.md
 * Determines whether [s] can be segmented into a sequence of one or more
 * dictionary words from [wordDict]. Every character of [s] must be covered:
 * the words must tile [s] contiguously, in order, with no characters left
 * over. Each dictionary word may be used zero or more times.
 *
 * @param s the input string
 * @param wordDict the set of allowed words; each word is reusable
 * @return `true` if [s] can be split entirely into words drawn from [wordDict]
 *   with no characters left unused, `false` otherwise
 */
fun wordBreak(s: String, wordDict: Set<String>): Boolean {

    val reachable = BooleanArray(s.length + 1)
    reachable[0] = true

    for (i in 0 until s.length) {
        if (reachable[i]) {
            for (j in i until s.length) {
                val candidateWord = s.substring(i, j+1)
                if (wordDict.contains(candidateWord)) {
                    reachable[j + 1] = true
                }
            }
        }
    }
    return reachable[s.length]
}


/**
 * Returns every possible way to segment [s] into dictionary words from
 * [wordDict], joining the words of each segmentation with a single space.
 *
 * @param s the input string
 * @param wordDict the set of allowed words
 * @return the list of all valid segmentations of [s]; an empty list if [s]
 *   cannot be segmented
 */
fun wordBreakIi(s: String, wordDict: Set<String>): List<String> {
    if (s.isEmpty()) return emptyList()
    // memoized DFS: returns all segmentations of s[startIndex..end)
    val memo = mutableMapOf<Int, List<String>>()

    fun dfs(startIndex: Int): List<String> {
        if (startIndex == s.length) return listOf("")
        if (startIndex in memo) return memo.getValue(startIndex)
        val sentences = mutableListOf<String>()
        for (endIndex in startIndex + 1..s.length) {
            val candidateWord = s.substring(startIndex, endIndex)
            if (candidateWord in wordDict) {
                for (suffix in dfs(endIndex)) {
                    sentences.add(if (suffix.isEmpty()) candidateWord else "$candidateWord $suffix")
                }
            }
        }
        memo[startIndex] = sentences
        return sentences
    }

    return dfs(0)
}


fun main() {
    data class Test(val case: String, val expected: Boolean, val actual: Boolean) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }
    Test(
        case = "Simple single word string and dict",
        expected = true,
        actual = wordBreak("hello", setOf("hello"))
    )
    Test(
        case = "Long input with repeating words all present",
        expected = true,
        actual = wordBreak("myhellomylovelyhello", setOf("hello", "my", "lovely"))
    )
    Test(
        case = "Not present word",
        expected = false,
        actual = wordBreak("hello", setOf("d"))
    )
    Test(
        case = "Long input with repeating words all present, one extra tail letter",
        expected = false,
        actual = wordBreak("myhellomylovelyhelloo", setOf("hello", "my", "lovely"))
    )
    Test(
        case = "Long input with repeating words all present, one extra head letter",
        expected = false,
        actual = wordBreak("hmyhellomylovelyhello", setOf("hello", "my", "lovely"))
    )


    data class SentenceTest(
        val case: String,
        val expected: List<String>,
        val actual: List<String>
    ) {
        init {
            // sentence order from DFS is deterministic but we compare as sets to be safe
            val passed = expected.toSet() == actual.toSet()
            if (!passed) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    SentenceTest(
        case = "Single word yields one sentence",
        expected = listOf("cats"),
        actual = wordBreakIi("cats", setOf("cats"))
    )
    SentenceTest(
        case = "Classic catsanddog yields two sentences",
        expected = listOf("cats and dog", "cat sand dog"),
        actual = wordBreakIi("catsanddog", setOf("cat", "cats", "and", "sand", "dog"))
    )
    SentenceTest(
        case = "Empty input yields no sentences",
        expected = emptyList(),
        actual = wordBreakIi("", setOf("a"))
    )
    SentenceTest(
        case = "Unsegmentable input yields no sentences",
        expected = emptyList(),
        actual = wordBreakIi("hello", setOf("d"))
    )
}
