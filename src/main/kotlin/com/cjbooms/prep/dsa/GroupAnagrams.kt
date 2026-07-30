package com.cjbooms.prep.dsa

/**
 * Group Anagrams (LC 49): group words that are anagrams of each other.
 *
 * Key: sorted characters (or letter-count signature). O(n * k log k) time,
 * n = words, k = max word length.
 */
fun groupAnagrams(words: List<String>): List<List<String>> =
    words.groupBy { it.toCharArray().sorted() }.values.toList()
