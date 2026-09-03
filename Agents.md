# Agents.md — MongoDB Staff+ Interview Prep

## What this repo is

Kotlin practice workspace for MongoDB Staff+ onsite interviews. The user is preparing for a retake after failing a topological-sort DSA round. The repo contains:

- **Practice drills** (`src/main/kotlin/com/cjbooms/prep/stages/`) — TODO-body skeletons with KDoc. The user implements these; JUnit5 tests tell them when they're right.
- **Solved reference** (`src/main/kotlin/com/cjbooms/prep/solutions/`) — complete working implementations. For checking answers after the user has solved (or attempted) a drill.

## The homework rule

**Never implement the drills for the user.** The learning value is in the user writing the code and getting test feedback. Your job is to:

1. Point them to the right stage file and its KDoc.
2. Help them run the tests (`./gradlew test --tests '*stages.stageN*'`).
3. Debug failures with hints, not solutions.
4. After they've solved it, compare with `solutions/` if they want a reference.

## Repo layout

```
src/main/kotlin/com/cjbooms/prep/
  stages/          # TODO stubs — user implements these
    stage0/        # Rate limiter ladder (done)
    stage1/        # DSA structures: interval merge, tree serialize, connected components, KV+TTL, topo sort
    stage2/        # Read-write lock (done)
    stage3/        # Iterators, JSON parser, inverted index (done except InvertedIndex stub)
    stage4/        # MongoDB internals lab (skeleton)
    stage5/        # Code review practice (done)
    stage8/        # Graph algorithms: topo sort (Kahn + DFS), cycle detect, BFS shortest path, union-find, course schedule, alien dict
    stage9/        # Tree algorithms: BST validate, LCA, Trie, BST serialize
    stage10/       # Array/string: two pointers, sliding window, binary search, prefix sums
    stage11/       # Heap: kth largest, merge k lists, task scheduler w/ cooldown
    stage12/       # Concurrency: bounded blocking queue, connection pool, lock-free counter
    stage13/       # System DSA: LRU cache, hit counter, consistent hashing
    stage14/       # Dynamic programming: basic, string, interval
    stage15/       # Cold-recall refreshers: rate limiter, interval merge, tree serialize, RW lock
  solutions/       # Solved reference implementations (same stage structure)
  dsa/             # Older solved exercises (VersionedKVStore, WordBreak, KSum, GroupAnagrams)
  concurrency/     # Older solved exercises (BoundedBlockingQueue, ThreadSafeLruCache, RaceConditionFix)
  realworld/       # Older solved exercises (ReplicationLagAlerter)
src/test/kotlin/com/cjbooms/prep/stages/   # Tests — fail with NotImplementedError until user implements
```

## How to guide a study session

1. **Pick a stage** based on the user's time budget and weak areas. The priority gap is stage 8 (graph algorithms).
2. **Point to the file**: "Open `src/main/kotlin/com/cjbooms/prep/stages/stage8/TaskScheduler.kt` and read the KDoc."
3. **Let them implement**. Do not write the body for them.
4. **Run tests**: `./gradlew test --tests '*stages.stage8*'`
5. **Debug failures**: read the test expectation, read their code, ask guiding questions. Do not paste the solution.
6. **Compare with reference** (optional): after they pass, they can diff their implementation against `solutions/stageN/`.

## Important: test expectations are correct

The test files in `src/test/kotlin/com/cjbooms/prep/stages/` contain **corrected** expectations. During initial creation, ~11 skeleton tests had mathematically wrong expected values; those were fixed. Do not change test assertions unless you find a genuine bug — and if you do, verify by hand before editing.

## Commands

```bash
./gradlew test                                    # full suite (stages 0-7 pass, 8-15 fail on stubs)
./gradlew test --tests '*stages.stage8*'          # one stage
./gradlew test --tests '*stages.stage8.TaskSchedulerTest*'  # one class
```

## Stage time budgets

| Stage | Topic | Budget |
|-------|-------|--------|
| 8 | Graph algorithms | 90 min |
| 8.5 | Graph practice | 30 min |
| 9 | Tree algorithms | 60 min |
| 10 | Array/string | 60 min |
| 11 | Heap | 45 min |
| 12 | Concurrency | 45 min |
| 13 | System DSA | 60 min |
| 14 | Dynamic programming | 45 min |
| 15 | Cold recall | 75 min |

## What "done" looks like

A stage is done when its tests pass. The user should be able to explain the structure-selection ritual aloud (why this data structure, what the invariants are, what the follow-up mutations would be).
