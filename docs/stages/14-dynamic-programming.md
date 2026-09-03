# Stage 14 — Dynamic Programming

**Time budget: 45 min** · Package: `stages/stage14`

## The meta-skill

Half the credit for a DP question is stating the subproblem clearly BEFORE
coding: what does dp[i] MEAN, what's the recurrence, what's the base case?
Say it aloud, then code. The other half is recognizing when greedy suffices —
if a local choice is provably safe, don't reach for the table.

## Exercises

### 14.1 Basic patterns (15 min)
`climbStairs(n)` / `houseRobber(nums)` in `BasicDp.kt`
- [ ] Stairs: dp[i] = dp[i-1] + dp[i-2] — Fibonacci; roll two variables, O(1) space
- [ ] Robber: dp[i] = max(dp[i-1], dp[i-2] + nums[i]) — skip or take
- [ ] Aloud: memoization vs tabulation — same recurrence, different direction

### 14.2 String DP (15 min)
`longestCommonSubsequence(a, b)` / `editDistance(a, b)` in `StringDp.kt`
- [ ] LCS: 2D table on prefixes; match -> diagonal+1, else max(up, left)
- [ ] Edit distance: insert/delete/replace = three neighbors + 1
- [ ] Space optimization: two rows suffice — O(min(m, n))
- [ ] MongoDB framing: schema migration, config diffing

### 14.3 Interval DP (15 min)
`matrixChainOrder(dimensions)` / `maxCoinsBurst(nums)` in `IntervalDp.kt`
- [ ] dp[i][j] = best over split k of dp[i][k] + dp[k][j] + cost(i, k, j)
- [ ] Fill by increasing interval length — say why (subproblems must be ready)
- [ ] Burst balloons: think LAST balloon to burst, not first
- [ ] Aloud: when intervals need DP vs greedy (no safe local choice)

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage14*'`); each exercise committed separately
- [ ] For each problem you can state dp[i] (or dp[i][j]) in one sentence before coding

## Commit points
After 14.1, after 14.3.
