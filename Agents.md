# Agents.md — MongoDB Staff+ Interview Prep

## What this repo is

Kotlin practice workspace for MongoDB Staff+ onsite interviews. Practice drills live in `stages/` as TODO-body skeletons with KDoc; the user implements them and JUnit5 tests provide the feedback loop. Solved reference implementations live in `solutions/` for post-exercise comparison.

## Scope: DSA only

**Complete — do not revisit:** technical screen, code review (stage 5), system design (stage 6), experience deep dive (stage 7). Future sessions focus exclusively on DSA exercises (stages 0–4 and 8–15). Only revisit a non-DSA stage if the user explicitly asks.

## The homework rule

**Never implement the drills for the user.** The learning value is in the user writing the code and getting test feedback. "Implement stage N" in any plan means **guide the user to implement it** — never copy bodies from `solutions/` into `stages/`.

Your job:

1. Point them to the right stage file and its KDoc.
2. Help them run the tests (`./gradlew test --tests '*stages.stageN*'`).
3. Debug failures with hints, not solutions.
4. After they've solved it, compare with `solutions/` if they want a reference.

## Repo layout

```
src/main/kotlin/com/cjbooms/prep/
  stages/          # TODO stubs — user implements these
    stage0/        # Rate limiter ladder
    stage1/        # DSA structures: interval merge, tree serialize, connected components, KV+TTL, topo sort
    stage2/        # Read-write lock
    stage3/        # Iterators, JSON parser, inverted index
    stage4/        # MongoDB internals lab
    stage5/        # Code review practice
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
src/test/kotlin/com/cjbooms/prep/stages/   # Tests — fail with NotImplementedError until user implements
docs/stages/       # Stage docs with time budgets, rituals, and "Done when" checklists
```

## How progress is tracked

- **Stage docs** (`docs/stages/*.md`) — one per stage, each with per-exercise `- [ ]` checkboxes, a "Done when" section, and "Commit points". The user checks items off as they go.
- **README stage table** — index of all stages with links, topics, and time budgets.
- **Tests** are the ground truth: a stage is done when `./gradlew test --tests '*stages.stageN*'` is green.
- **Commit history** shows which exercises have been completed (docs specify per-exercise commit points).

## How to guide a study session

1. **Pick a stage** based on the user's time budget and weak areas.
2. **Open the stage doc** (`docs/stages/NN-*.md`) — it has the exercises, rituals, and checkboxes for the session.
3. **Point to the exercise file**: "Open `src/main/kotlin/com/cjbooms/prep/stages/stage8/TaskScheduler.kt` and read the KDoc."
4. **Let them implement**. Do not write the body for them.
5. **Run tests**: `./gradlew test --tests '*stages.stage8*'`
6. **Debug failures**: read the test expectation, read their code, ask guiding questions. Do not paste the solution.
7. **Check off the doc's checkbox** and commit at the doc's commit point.
8. **Compare with reference** (optional): after they pass, they can diff their implementation against `solutions/stageN/`.

## Important: test expectations are correct

The test files in `src/test/kotlin/com/cjbooms/prep/stages/` contain **corrected** expectations. During initial creation, several skeleton tests had mathematically wrong expected values; those were fixed. Do not change test assertions unless you find a genuine bug — and if you do, verify by hand before editing.

## Commands

```bash
./gradlew test                                    # full suite
./gradlew test --tests '*stages.stage8*'          # one stage
./gradlew test --tests '*stages.stage8.TaskSchedulerTest*'  # one class
```

The user self-times exercises — no mock-drill apparatus; just run the tests.

## Stage time budgets

| Stage | Topic | Budget |
|-------|-------|--------|
| 8 | Graph algorithms (incl. 8.5 practice) | 90 + 30 min |
| 9 | Tree algorithms | 60 min |
| 10 | Array/string patterns | 60 min |
| 11 | Heap / priority queue | 45 min |
| 12 | Concurrency primitives | 45 min |
| 13 | System-adjacent DSA | 60 min |
| 14 | Dynamic programming | 45 min |
| 15 | Cold recall | 75 min |

## What "done" looks like

A stage is done when its tests pass. The user should be able to explain the structure-selection ritual aloud (why this data structure, what the invariants are, what the follow-up mutations would be).
