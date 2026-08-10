# MongoDB Staff+ Interview Prep — Atlas Clusters Onsite

Kotlin practice workspace for the **onsite panel** (screen passed ✅: rate
limiter, sliding-window deque + map, O(1) allow).

## The panel

1. **Programming** — CoderPad, DS selection, complexity, mid-round mutations
2. **System Design** — Atlas-scale, whiteboard via CoderPad
3. **Code Review** — junior PR, planted issues, depth > breadth
4. **Experience Deep Dive** — architecture scope, production impact

## How this repo works

8 stages, ~8 hours total. Each stage has a doc in `docs/stages/` (goals,
exercises, time budget, homework, done-when checklist) and a matching package
in `src/main/kotlin/com/cjbooms/prep/stages/` with skeletons — **signatures +
KDoc only, no solutions** (homework rule: you write the code, tests tell you
when it's right).

| Stage | Content | Budget |
|---|---|---|
| [0](docs/stages/00-rate-limiter-ladder.md) | Rate-limiter retrospective + escalation ladder | 45m |
| [1](docs/stages/01-dsa-structures.md) ⭐ | DSA set A: choosing the right structure | 90m |
| [2](docs/stages/02-read-write-lock.md) | Read-Write Lock (verified onsite question) | 45m |
| [3](docs/stages/03-iterators-parsers.md) ⭐ | DSA set B: iterators, JSON parser, inverted index | 90m |
| [4](docs/stages/04-mongodb-fluency.md) ⭐ | MongoDB internals + Docker replica-set lab | 75m |
| [5](docs/stages/05-code-review.md) | Code review: protocol + planted-bug PR | 60m |
| [6](docs/stages/06-system-design.md) | System design (light, flex stage) | 45m |
| [7](docs/stages/07-deep-dive-gauntlet.md) | Deep-dive checklist + final gauntlet | 30m |

## Reference docs

- `docs/cheat-sheet.md` — DS/algorithms/concurrency flash cards
- `docs/mongodb-internals.md` — internals primer (Stage 4 reading)
- `docs/review-protocol.md` — code review narration protocol (Stage 5)
- `docs/reading-go.md` — Go primer in case the review sample is Go
- `docs/design-skeleton.md` + `docs/designs/` — system design spine + 2 designs
- `docs/exercise-prompts.md`, `docs/coderpad-drills.md` — screen-prep drills
  (still useful for warm-ups)

## Commands

```bash
./gradlew build                                # compile + all tests
./gradlew test --tests '*stages.stage1*'       # one stage's tests
docker compose up -d                           # Stage 4 replica set
```

## From the screen prep (still valid)

`dsa/`, `concurrency/`, `realworld/` packages with solved, commented exercises
(VersionedKVStore, BoundedBlockingQueue, ReplicationLagAlerter...) — reference
material and warm-up reps.
