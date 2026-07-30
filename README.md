# MongoDB Staff+ Interview Prep

Kotlin practice workspace for MongoDB live-coding interviews (CoderPad, no AI
assistance, 45–60 min rounds with executed code).

## Layout

| Path | Contents |
|---|---|
| `src/main/kotlin/com/cjbooms/prep/dsa/` | VersionedKVStore (reported MongoDB question), GroupAnagrams, WordBreak, KSum |
| `src/main/kotlin/com/cjbooms/prep/concurrency/` | BoundedBlockingQueue (lock+conditions and synchronized variants), ThreadSafeLruCache, RaceConditionFix (broken/fixed pairs) |
| `src/main/kotlin/com/cjbooms/prep/realworld/` | ReplicationLagAlerter (reported MongoDB real-world round) |
| `docs/exercise-prompts.md` | Interview-style problem statements + starter signatures + follow-ups per exercise |
| `docs/system-design.md` | Design-round talking points: sharded KV store, DB migration platform |
| `docs/coderpad-drills.md` | Timed-drill protocol + follow-up mutations to self-impose |

## Commands

```bash
./gradlew build          # compile + run all tests
./gradlew test           # tests only
./gradlew test --tests '*VersionedKVStoreTest*'   # single exercise
```

Open in IntelliJ IDEA — it will sync via the Gradle wrapper (no local Gradle
install needed).

## How to practice

See `docs/coderpad-drills.md` for the timed-drill protocol and
`docs/exercise-prompts.md` for the problem statements — read the prompt, work from
a blank buffer, and only look at the solution in `src/main/kotlin/` afterwards.
