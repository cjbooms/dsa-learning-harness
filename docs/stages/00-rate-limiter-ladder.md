# Stage 0 — Rate Limiter Retrospective + Escalation Ladder

**Time budget: 30 min** · Package: `stages/stage0` · Verified target: "concurrent expiring queue" (Glassdoor, Oct 2025)

## Why this stage

Your screen solution was a sliding-window rate limiter (deque + map, O(1) `allow`).
Interviewers escalate this exact question. Each rung below is a real reported
follow-up pattern. Rebuild from memory first — cold recall is the training.

## Exercises

### 0.1 Rebuild from memory (10 min)
- [ ] Implement `RateLimiter(maxRequests: Int, perMillis: Long)` with
  `fun allow(requestId: String, nowMillis: Long): Boolean` in `RateLimiter.kt`
- [ ] Sliding-window log: drop expired entries from the front, reject when full
- [ ] Target: O(1) amortized per call. Say why aloud (each request enters/leaves once)
- [ ] Make `RateLimiterTest` pass (skeleton provided)

### 0.2 Rung 1 — thread-safe (5 min)
- [ ] Multiple threads call `allow` concurrently. Fix it. Which tool and why?
  (Lock vs semaphore vs atomic — narrate the choice)

### 0.3 Rung 2 — per-user limits (5 min)
- [ ] `allow(userId, requestId, nowMillis)` — each user gets their own window
- [ ] What structure? What happens to memory as users grow?

### 0.4 Rung 3 — memory bounding (5 min)
- [ ] Evict idle users. What's the cheapest correct eviction strategy?
  (Hint: you already have per-user last-activity — where?)

### 0.5 Rung 4 — token bucket variant + distributed discussion (5 min)
- [ ] Sketch `TokenBucketRateLimiter` (no code needed — API + refill math in comments)
- [ ] Aloud: sliding log vs token bucket — space, burst behavior, precision
- [ ] Aloud: "now 10 app servers share the limit" — what moves where?
  (This is the mini-system-design; 3 sentences is enough)

## Done when
- [ ] All tests green, each rung committed separately
- [ ] You can narrate the full ladder in under 3 minutes without notes

## Commit points
After 0.1, after 0.3, after 0.5.
