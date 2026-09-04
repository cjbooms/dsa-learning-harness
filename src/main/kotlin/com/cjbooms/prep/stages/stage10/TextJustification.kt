package com.cjbooms.prep.stages.stage10

/**
 * Learn first: see docs/learning-resources.md
 * Stage 10.5 — Text Justification.
 */

/**
 * Format `words` into fully-justified text of width `maxWidth`. Every line
 * must contain exactly `maxWidth` characters (counting spaces between words).
 *
 * Rules:
 *   - Greedy packing: each line contains as many words as will fit, joined by
 *     single spaces.
 *   - For all lines except the last, and any line containing a single word,
 *     extra spaces are distributed evenly between words. Leftover spaces
 *     (when the count does not divide evenly) go into the leftmost gaps, one
 *     extra space per gap from left to right.
 *   - The last line is left-justified: a single space between words, with all
 *     remaining space as trailing spaces on the right (it may therefore
 *     contain fewer than `maxWidth` non-space characters plus a single
 *     trailing run of spaces).
 *
 * @param words the input words in order; `words.length >= 1`
 * @param maxWidth the exact width of every output line; must be at least as
 *                 wide as the longest single word
 * @return a list of formatted lines, one entry per output line
 */
fun textJustify(words: Array<String>, maxWidth: Int): List<String> {
    TODO("implement")
}
