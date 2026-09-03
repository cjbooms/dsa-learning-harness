# Stage 5 — Code Review Prep (protocol-driven)

**Status: ✅ DONE — do not revisit unless the user asks.** · **Time budget: 60 min** · Package: `stages/stage5`

## Why protocol-first

Code reviews feel bad because they're open-ended performances. The fix is a
repeatable procedure. Read `docs/review-protocol.md` FIRST (10 min) — it gives
you the scan order, the comment style, and the narration phrases.

## Exercises

### 5.1 Cold review (25 min)
- [ ] Open `stages/stage5/juniorpr/SessionCache.kt` — a junior colleague's PR
- [ ] Do NOT look at `Manifest.kt.txt` (the answer key lives there)
- [ ] Review exactly as the protocol says: scan for the business goal first,
  leave `//` comments as you go, severity-rank mentally
- [ ] Set a timer: 10 min silent reading, 15 min narrating findings aloud
  (to the room, to a rubber duck — out loud is the training)

### 5.2 Score yourself (15 min)
- [ ] NOW open the manifest. For each planted issue: found / missed
- [ ] For each miss: which protocol step would have caught it?
- [ ] Notice: the manifest has SEVERITIES. Did you lead with the severe ones?

### 5.3 Self-review drill (10 min) — the VERIFIED Staff format
- [ ] Open your own Stage 1–3 code (written days ago = cold enough)
- [ ] Find 3 things you'd change. Say them as review comments, not rewrites:
  "I'd suggest X because Y" — collaborative framing, not self-flagellation

## Done when
- [ ] You found at least the 3 highest-severity planted issues
- [ ] You can state the protocol's scan order from memory

## Commit points
After 5.2 (your annotated review comments in the file are the artifact).
