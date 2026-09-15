# Using this repo

## What this repo is

Kotlin practice workspace for DSA and system-adjacent drills. Exercises live in
`stages/` as TODO-body skeletons with KDoc; you implement them and JUnit5 tests
provide the feedback loop. Solved reference implementations live in `solutions/`
for post-exercise comparison.

## Scope

Active coding stages: 0–4 and 8–15 (DSA, concurrency, system-adjacent structures).

## The learning rule

**Never copy from `solutions/` into `stages/` before attempting the exercise.**
The value is in writing the code and getting test feedback. After you've solved
it, compare with `solutions/` if you want a reference.

## Repo layout

```
src/main/kotlin/com/cjbooms/prep/
  stages/          # TODO stubs — you implement these
    stage0/        # Rate limiter ladder
    stage1/        # DSA structures: interval merge, tree serialize, connected components, KV+TTL, topo sort
    stage2/        # Read-write lock
    stage3/        # Iterators, JSON parser, inverted index
    stage4/        # Database internals fluency
    stage8/        # Graph algorithms (incl. 8.5 practice: CourseSchedule, AlienDictionary)
    stage9/        # Tree algorithms
    stage10/       # Array/string patterns
    stage11/       # Heap / priority queue
    stage12/       # Concurrency primitives
    stage13/       # System-adjacent DSA
    stage14/       # Dynamic programming
    stage15/       # Cold-recall refreshers
  solutions/       # Solved reference implementations (same stage structure)
  dsa/             # Older solved exercises
  concurrency/     # Older solved exercises
  realworld/       # Older solved exercises
src/test/kotlin/com/cjbooms/prep/stages/   # Tests — fail with NotImplementedError until you implement
docs/stages/       # Stage docs with exercises and notes
```

## How progress is tracked

- **Stage docs** (`docs/stages/*.md`) — one per stage, each with per-exercise
  `- [ ]` checkboxes and a "Check your understanding" section.
- **README stage table** — index of all stages with links, topics, and estimates.
- **Tests** are the ground truth: a stage is solved when
  `./gradlew test --tests '*stages.stageN*'` is green.

## How to run a study session

1. Grep `src/main/kotlin/com/cjbooms/prep/stages` for `TODO`/`NotImplementedError`
   stubs to find remaining work.
2. Cross-reference with `git log --oneline` to see what you've already completed.
3. Pick the smallest next gap and open that file.
4. Open the stage doc (`docs/stages/NN-*.md`) for context and notes.
5. Implement the exercise.
6. Run tests with a forced rerun: `./gradlew cleanTest test --tests '*stages.stageN*'`.
   Plain `./gradlew test` can report `UP-TO-DATE` and mislead you.
7. Check off the doc's checkbox when tests pass.
8. Compare with reference (optional): after you pass, diff your implementation
   against `solutions/stageN/`.

## Commands

```bash
./gradlew test                                    # full suite
./gradlew test --tests '*stages.stage8*'          # one stage
./gradlew test --tests '*stages.stage8.TaskSchedulerTest*'  # one class
```

You self-time exercises — just run the tests.

## Stage time estimates

| Stage | Topic | Estimate |
|-------|-------|----------|
| 8 | Graph algorithms (incl. 8.5 practice) | 90 + 30 min |
| 9 | Tree algorithms | 60 min |
| 10 | Array/string patterns | 60 min |
| 11 | Heap / priority queue | 45 min |
| 12 | Concurrency primitives | 45 min |
| 13 | System-adjacent DSA | 60 min |
| 14 | Dynamic programming | 45 min |
| 15 | Cold recall | 75 min |

## What "solved" looks like

A stage is solved when its tests pass. You should be able to explain the
structure-selection reasoning aloud: why this data structure, what the
invariants are, and what follow-up mutations would look like.
