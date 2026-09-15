package com.cjbooms.prep.dsa

/**
 * Group Anagrams (LC 49): group words that are anagrams of each other.
 *
 * Two approaches below. Lead with the sorted-key one (simpler),
 * then mention the count-key one when probed on complexity — and be ready to
 * implement it as a follow-up.
 */

/**
 * Approach 1: sort each word's characters to build the group key.
 *
 * Anagrams are exactly the words whose sorted characters are equal:
 *   "eat" -> "aet", "tea" -> "aet"  => same group.
 *
 * Time:  O(n * k log k)   n = number of words, k = longest word (the per-word sort)
 * Space: O(n * k)         every word stored in the map
 */
fun groupAnagrams(words: List<String>): List<List<String>> {
    val groupsBySortedKey = HashMap<String, MutableList<String>>()

    for (word in words) {
        // Sorting the characters gives a canonical key shared by all anagrams.
        val sortedChars = word.toCharArray()
        sortedChars.sort()
        val groupKey = String(sortedChars)

        val group = groupsBySortedKey.getOrPut(groupKey) { mutableListOf() }
        group.add(word)
    }

    return groupsBySortedKey.values.toList()
}

/**
 * Approach 2 (the follow-up they'd ask for): letter-count key, avoiding the sort.
 *
 * The key is the 26 letter frequencies, e.g. "eat" -> "1#0#0#0#1#...#1#...".
 * Two words are anagrams iff their frequency vectors are identical.
 *
 * Time:  O(n * k)  — linear pass per word instead of a sort.
 * Worth mentioning the trade-off aloud: constant-factor win, uglier key,
 * and only valid because the alphabet is small and known (a-z).
 */
fun groupAnagramsByCount(words: List<String>): List<List<String>> {
    val groupsByCountKey = HashMap<String, MutableList<String>>()

    for (word in words) {
        val letterCounts = IntArray(26)
        for (char in word) {
            val letterIndex = char - 'a'
            letterCounts[letterIndex]++
        }
        // '#' separators prevent ambiguity: counts 1 and 11 must not collide
        // with count 111 in the flattened string.
        val groupKey = letterCounts.joinToString("#")

        val group = groupsByCountKey.getOrPut(groupKey) { mutableListOf() }
        group.add(word)
    }

    return groupsByCountKey.values.toList()
}
