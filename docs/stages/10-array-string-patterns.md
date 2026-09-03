# Stage 10 — Array/String Patterns

**Time budget: 75 min** · Package: `stages/stage10`

## The meta-skill

Four patterns cover most array/string questions: two pointers (sorted input,
pairwise constraints), sliding window (contiguous optimal subrange), binary
search (monotonic predicate), prefix sums (range queries / sum equals k).
The ritual: name the pattern BEFORE coding and say what makes it applicable.

## Exercises

### 10.1 Two pointers (15 min)
`pairSumSorted` / `maxArea` / `removeDuplicatesSorted` in `TwoPointers.kt`
- [ ] Sorted pair sum: converge from both ends — say why each move is safe
- [ ] Container with most water: move the SHORTER wall — defend why
- [ ] Remove duplicates in place: slow/fast pointers, return new length

### 10.2 Sliding window (15 min)
`longestSubstringWithoutRepeats` / `minWindowSubstring` in `SlidingWindow.kt`
- [ ] Variable window: last-seen index map, jump the left edge
- [ ] Min window: need/have counters — the two-phase expand/contract

### 10.3 Binary search variants (15 min)
`searchRotated` / `findPeakElement` / `search2DMatrix` in `BinarySearchVariants.kt`
- [ ] Rotated array: one half is always sorted — check which, then decide
- [ ] Peak: move toward the rising slope
- [ ] 2D matrix: flatten indices, mid -> matrix[mid/cols][mid%cols]

### 10.4 Prefix sums (10 min)
`ImmutableArraySum.sumRange` / `subarraySumEqualsK` in `PrefixSums.kt`
- [ ] prefix[i] = sum of nums[0..<i]; range query = difference
- [ ] Sum-equals-k: prefix-sum frequency map — say why the map works

### 10.5 Text justification (15 min)
`textJustify(words, maxWidth)` in `TextJustification.kt`
- [ ] Greedy line packing: pack as many words as fit, then distribute spaces
- [ ] Last line and single-word lines are left-justified
- [ ] Aloud: why this is greedy, not DP

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage10*'`); each exercise committed separately
- [ ] For each exercise you can name the pattern and the monotonicity/invariant that powers it
- [ ] You can explain why text justification is greedy and why sliding-window questions are not

## Commit points
After 10.2, after 10.4, after 10.5.
