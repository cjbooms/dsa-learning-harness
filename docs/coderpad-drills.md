# CoderPad Drill Protocol

How to use this repo to simulate the real round: **45-minute timed drills, no AI,
code must run.** CoderPad gives you an editor + run button and not much else —
practice without relying on autocomplete or an assistant.

## Before each drill

1. Pick one exercise. Close this repo's solution file — work from a blank buffer.
2. Set a 45-minute timer.
3. Keep a scratch terminal with `./gradlew test --tests '*<ExerciseTest>*'` ready,
   or paste code into a standalone `main` and run it — CoderPad style.

## During the drill (narrate everything aloud)

1. **Clarify** (5 min): restate the problem, ask/assume edge cases, agree on the API
   signature. Write these as comments.
2. **Plan** (5 min): state the approach + complexity before coding. Offer the naive
   option first, then the better one, and say why you're picking it.
3. **Implement** (20 min): clean, idiomatic Kotlin. Name things well. No cleverness.
4. **Test** (10 min): walk through the examples, then edge cases — empty input,
   single element, duplicates, boundaries, out-of-order events.
5. **Review** (5 min): state final complexity; say what you'd do with more time.

## After: self-imposed follow-up mutations

Interviewers change the rules mid-round. Redo each exercise with one mutation:

| Exercise | Mutations to practice |
|---|---|
| VersionedKVStore | puts out of order; add `delete(docId, ts)`; bound memory (evict old versions); make it thread-safe |
| BoundedBlockingQueue | add timed `offer(item, timeout)`; multiple consumer groups; fair (FIFO) wakeup; bounded executor with blocking `submit()`; re-implement with **two semaphores + a lock** and compare trade-offs aloud |
| ThreadSafeLruCache | lock striping; per-entry TTL; async loading (LoadingCache); weigh entries by size |
| ReplicationLagAlerter | events stream in from multiple shards out of order; exactly-once alerting under retries; sliding-window *average* lag instead of per-record |
| WordBreak / KSum | return all segmentations / all tuples; streaming input; memory bound |

## Interview-day checklist

- [ ] Confirm with recruiter: CoderPad language support, whether code is executed,
      camera/screen-share expectations.
- [ ] Test CoderPad's Kotlin environment beforehand (it lags latest Kotlin versions —
      avoid bleeding-edge syntax).
- [ ] Think out loud continuously; silence reads as being stuck.
- [ ] If hint offered, take it — collaboration is scored positively.
- [ ] State complexity unprompted; it's expected at Staff+.
