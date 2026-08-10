# Stage 4 — MongoDB Internals Fluency + Docker Lab

**Time budget: 75 min** ⭐ · Package: `stages/stage4` · Evidence: verified SDE5
failure report — "study how modern DBs work in depth"

## Why this stage

At a database company, on a database team, generic distributed-systems talk
doesn't differentiate you. MongoDB vocabulary and hands-on experience do.
Output of this stage: `docs/lab-notes.md` with YOUR observations — those become
talking points in every other round.

## Part A — Active reading (25 min)

Read `docs/mongodb-internals.md`. Don't skim — for each section:
- [ ] Write one question an interviewer might ask about it
- [ ] Write the 1-sentence answer you'd give
(Annotate directly in a scratch file or the margins)

## Part B — Hands-on lab (45 min)

Setup: `docker compose up -d` from repo root, then initiate the replica set
(command in the stage4 package KDoc / MongoLab.kt).

### Lab 1 — write concerns (15 min)
- [ ] Insert with w:1 vs w:majority (MongoLab.kt skeleton has the calls)
- [ ] Observe latency difference. Record numbers in lab-notes.md
- [ ] Aloud: what does w:majority actually guarantee? (survives failover)

### Lab 2 — change streams (15 min)
- [ ] Tail the oplog from Kotlin while inserting from a second thread
- [ ] Recognize: this IS the CDC mechanism behind your migration exercises
- [ ] Record one sample change event in lab-notes.md

### Lab 3 — kill the primary (15 min)
- [ ] Write continuously (w:1), `docker kill` the primary mid-stream
- [ ] Watch: how long until writes succeed again? (election time)
- [ ] Did any acknowledged writes disappear? (rollback of un-replicated writes)
- [ ] Repeat with w:majority. Difference? THIS is the failover-orchestrator
  design question, experienced firsthand

## Homework (not in budget)
- [ ] explain("executionStats") on an indexed vs unindexed query — COLLSCAN vs IXSCAN

## Done when
- [ ] lab-notes.md has: election time measurement, w:1 vs w:majority latency,
  one change event, one rollback observation
- [ ] You can explain oplog vs WAL vs change stream without notes

## Commit points
After lab-notes.md is complete.
