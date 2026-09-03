package com.cjbooms.prep.stages.stage10

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

/**
 * Formats [words] into lines of length [maxWidth], fully justifying every
 * line except the last one and single-word lines, which are left-justified.
 */
fun textJustify(words: Array<String>, maxWidth: Int): List<String> {
    TODO("implement")
}
