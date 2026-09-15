package com.cjbooms.prep.dsa

/**
 * Word Break (LC 139): can s be segmented into a sequence of dictionary words?
 * Dictionary words may be reused.
 *
 * DP over prefixes:
 *   canSegment[i]  =  can s[0 ..< i] be segmented?
 *   canSegment[0]  =  true (empty prefix needs no words)
 *   canSegment[i]  =  true if there is ANY split point j where
 *                     the left part s[0..<j] is already segmentable AND
 *                     the right part s[j..<i] is a dictionary word.
 *
 * Time:  O(n^2) split checks, each doing an O(k) substring build (k = word length)
 * Space: O(n) for the dp array.
 *
 * Tip: say the recurrence out loud BEFORE coding — half the credit
 * for a DP question is stating the subproblem clearly.
 */
fun wordBreak(s: String, wordDict: List<String>): Boolean {
    // Set for O(1) word membership instead of scanning the list each check.
    val dictionary = wordDict.toSet()

    // canSegment[i] == "the first i characters of s can be segmented"
    val canSegment = BooleanArray(s.length + 1)
    canSegment[0] = true // empty prefix: trivially segmentable

    for (prefixEnd in 1..s.length) {
        // Try every split: s[0..<splitPoint] | s[splitPoint..<prefixEnd]
        for (splitPoint in 0..<prefixEnd) {
            val leftPartSegmentable = canSegment[splitPoint]
            val rightPart = s.substring(splitPoint, prefixEnd)
            val rightPartInDictionary = rightPart in dictionary

            if (leftPartSegmentable && rightPartInDictionary) {
                canSegment[prefixEnd] = true
                break // one valid segmentation is enough for a boolean answer
            }
        }
    }

    return canSegment[s.length]
}
