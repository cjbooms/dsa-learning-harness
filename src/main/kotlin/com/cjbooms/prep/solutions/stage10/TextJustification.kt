package com.cjbooms.prep.solutions.stage10

/**
 * Stage 10.5 — Text Justification (15 min).
 *
 * MongoDB relevance: formatting query results, fixed-width CLI output, and
 * any text-layout problem where whitespace must be distributed evenly across
 * a known line width.
 *
 * Structure-selection ritual:
 *   - Greedy line packing is the right move: pack as many words as fit on
 *     each line, then distribute spaces.
 *   - Last line and single-word lines are special-cased (left-justified).
 *   - O(n) total: each word is placed exactly once.
 *
 * Time budget: 15 min.
 */

fun textJustify(words: Array<String>, maxWidth: Int): List<String> {
    require(maxWidth > 0) { "maxWidth must be positive, was $maxWidth" }
    val result = mutableListOf<String>()
    var i = 0

    while (i < words.size) {
        var j = i + 1
        var lineLength = words[i].length

        // Pack as many words as fit; account for the mandatory single space between words.
        while (j < words.size && lineLength + 1 + words[j].length <= maxWidth) {
            lineLength += 1 + words[j].length
            j++
        }

        val lineWords = words.slice(i until j)
        val numWords = lineWords.size
        val numSpaces = maxWidth - lineWords.sumOf { it.length }

        if (j == words.size || numWords == 1) {
            // Last line or single-word line: left-justify.
            result.add(lineWords.joinToString(" ").padEnd(maxWidth))
        } else {
            // Fully justify: distribute spaces as evenly as possible,
            // putting extra spaces in the leftmost slots.
            val gaps = numWords - 1
            val spacePerGap = numSpaces / gaps
            val extraSpaces = numSpaces % gaps
            val builder = StringBuilder()
            for (k in lineWords.indices) {
                builder.append(lineWords[k])
                if (k < gaps) {
                    builder.append(" ".repeat(spacePerGap + if (k < extraSpaces) 1 else 0))
                }
            }
            result.add(builder.toString())
        }

        i = j
    }

    return result
}
