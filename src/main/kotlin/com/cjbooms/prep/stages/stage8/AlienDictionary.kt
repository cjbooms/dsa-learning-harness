package com.cjbooms.prep.stages.stage8

import java.util.ArrayDeque

/**
 * Stage 8.5.2 — Alien dictionary: infer character ordering from a sorted
 * alien word list.
 *
 * Why this matters for MongoDB: collation rules for non-default locales, sort
 * comparators for custom indexes, ordering discovery between custom types.
 * The interview form: "given a dictionary of words in an unknown alphabet,
 * derive the alphabet order (or detect an invalid dictionary)".
 *
 * Structure-selection ritual:
 *   - Compare adjacent words left-to-right; the first differing character
 *     gives an edge u -> v ("u comes before v").
 *   - Run topological sort on the resulting graph.
 *   - Edge cases:
 *       * Duplicate adjacent words: fine, no new info.
 *       * Prefix relationship where later word is SHORTER and a prefix of
 *         the earlier word (e.g. ["abc", "ab"]): INVALID dictionary.
 *       * Disconnected components: alphabetical order is still meaningful as
 *         long as no cycle exists.
 *   - Return empty string ("") for invalid / cyclic dictionaries.
 *
 * Time budget: 15 min. Defend aloud: why topological sort, not just pairwise
 * comparisons? (Pairwise only gives local info; the graph captures global
 * ordering and exposes cycles.)
 */
fun alienOrder(words: List<String>): String {
    TODO("implement")
}
