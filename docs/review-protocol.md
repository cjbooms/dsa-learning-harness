# Code Review Narration Protocol

The round: junior colleague's PR, planted issues (severe + trivial), ~30 min
discussion, "depth over breadth." This protocol makes it a procedure, not a
performance.

## The scan (first 5–10 min, silent, leaving // comments)

Pass 1 — **Business goal** (2 min): What is this code FOR? Say it back to the
interviewer before critiquing: "So the goal is X, and correctness means Y."
Every finding then anchors to the goal — that's depth.

Pass 2 — **Correctness sweep** (5 min), in this fixed order:
1. **Concurrency**: shared mutable state? compound actions unsynchronized?
   (your training: lost update, check-then-act, visibility)
2. **Error handling**: swallowed exceptions? empty catch? errors returned but
   unchecked? partial failure leaving corrupt state?
3. **Boundaries**: off-by-one, empty input, null/absent cases, integer overflow
4. **Resources**: unclosed connections/files? leaks under exception?
5. **API/design**: mutable internals exposed? validation at the boundary?
   misleading names that hide behavior?

Leave `//` comments as you go — they're your memory when discussion jumps around.

## The narration (20 min)

- **Severe first.** Lead with the bug that corrupts data or deadlocks.
  "The biggest issue I see is X — here's the interleaving that breaks it..."
- **Show the failure, don't just name it.** One concrete input or thread
  schedule that breaks the code is worth three abstract concerns.
- **Ask vs assert** on the ambiguous ones: "I might be missing context — is
  this intentionally ordered?" Collaborative, not prosecutorial.
- **Group the trivia.** Ten style nits in one sentence: "plus a cluster of
  smaller things — naming, magic numbers — happy to list if useful."
  That signals breadth WITHOUT spending depth's budget.
- **Propose, don't just critique.** Every severe finding gets a one-line fix
  direction. The round's goal #3 is "propose alternatives."

## Time discipline

If 10 min pass on one theme and the interviewer redirects — follow them.
Being redirectable is scored. Unfinished notes are fine: "I had a few more
items in the error-handling area if we have time."

## Phrases that score

- "The business goal seems to be X — let me sanity-check my understanding."
- "Here's a concrete schedule that breaks it: T1..., T2..."
- "This one is severe because it silently corrupts..., whereas this one is
  cosmetic."
- "I'd suggest X because Y; the cost is Z."
