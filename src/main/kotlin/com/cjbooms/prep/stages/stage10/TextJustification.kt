package com.cjbooms.prep.stages.stage10

/**
 * Learn first: see docs/learning-resources.md
 * Stage 10.5 — Text Justification.
 */

/**
 * Format `words` into fully-justified text of width `maxWidth`. Every line
 * in the output — including the last — is exactly `maxWidth` characters.
 *
 * Rules:
 *   - Greedy packing: each line contains as many consecutive words as fit,
 *     where words w0..wk fit iff `sum(lengths) + k` single spaces
 *     `<= maxWidth` (one mandatory space between each adjacent pair).
 *   - For all lines except the last, and except single-word lines: the line
 *     is fully justified. The space pool is `maxWidth - sum(word lengths)`;
 *     after the mandatory single spaces, the extra spaces are distributed
 *     evenly across the `count - 1` gaps. Leftover spaces (when the pool
 *     does not divide evenly) go into the leftmost gaps, one extra space
 *     per gap from left to right.
 *   - Single-word lines (even mid-text) and the last line are
 *     left-justified: the word(s) joined by single spaces, then padded on
 *     the right with trailing spaces to exactly `maxWidth`.
 *
 * @param words the input words in order; `words.length >= 1`
 * @param maxWidth the exact width of every output line; must be at least as
 *                 wide as the longest single word
 * @return a list of formatted lines, one entry per output line
 */
fun textJustify(words: Array<String>, maxWidth: Int): List<String> {
    val results = mutableListOf<String>()
    var currentSentenceWords = mutableListOf<String>()
    var currentSetneceLetterCount = 0

    words.forEach { word ->
        if (currentSentenceWords.isNotEmpty() &&
            currentSetneceLetterCount +
            currentSentenceWords.size + // Spaces between words
            word.length >
            maxWidth
        ) {
            results.add(
                formatLine(currentSentenceWords, currentSetneceLetterCount, maxWidth, false)
            )
            currentSentenceWords.clear()
            currentSetneceLetterCount = 0
        }
        currentSentenceWords.add(word)
        currentSetneceLetterCount += word.length
    }
    results.add(
        formatLine(currentSentenceWords, currentSetneceLetterCount, maxWidth, true)
    )
    return results
}

private fun formatLine(words: List<String>, wordsLength: Int, maxWidth: Int, isLastLine: Boolean): String {
    // Rule: Last line or a single-word line is strictly left-justified
    if (isLastLine || words.size == 1) {
        return words.joinToString(" ").padEnd(maxWidth, ' ')
    }

    val totalSpaces = maxWidth - wordsLength
    val gaps = words.size - 1
    val spacesPerGap = totalSpaces / gaps
    var extraSpaces = totalSpaces % gaps // The remainder spaces that go to the leftmost gaps

    val builder = StringBuilder()
    for (i in 0 until words.size - 1) {
        builder.append(words[i])
        builder.append(" ".repeat(spacesPerGap))
        while (extraSpaces-- > 0) {
            builder.append(" ")
        }
    }
    builder.append(words.last()) // Final word has no trailing spaces

    return builder.toString()
}

fun main() {
    data class Test(val case: String, val expected: List<String>, val actual: List<String>) {
        init {
            if (expected != actual) println("FAILED $this")
            else println("PASSED $this")
        }
    }
    Test(
        case = "Very Good input",
        expected = listOf("Very  Good", "Input     "),
        actual = textJustify(arrayOf("Very", "Good", "Input"), 10),
    )
}
