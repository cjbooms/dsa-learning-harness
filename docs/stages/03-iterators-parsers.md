# Stage 3 — DSA Drill Set B: Iterators, Parsers, Indexes

**Time budget: 90 min** ⭐ top priority · Package: `stages/stage3`

## Exercises

### 3.1 Union of 2 sorted iterators (15 min) — VERIFIED shape (1P3A 2026)
`unionSorted(a: Iterator<Int>, b: Iterator<Int>): Iterator<Int>`
- Sorted inputs, sorted output, no duplicates, LAZY (don't materialize)
- The lazy part is the point: `hasNext()`/`next()` must do the work

### 3.2 Merge K sorted iterators (15 min) — the reported follow-up
`mergeKSorted(iterators: List<Iterator<Int>>): Iterator<Int>`
- Structure ritual: what holds the "current head of each iterator"?
- O(log k) per element — say why

### 3.3 JSON parser (35 min) — VERIFIED Staff question (Nov 2025)
`parse(json: String): JsonValue` — sealed class skeleton provided
- Tokenizer first (or single-pass with an index — your call, narrate it)
- Recursive descent: object, array, string, number, true/false/null
- Errors: what do you do on malformed input? (decide BEFORE coding)
- This one is long because it's the real reported Staff question. Take the time.

### 3.4 Inverted index (25 min) — REPORTED (Blind 2025, Atlas Search)
`insert(docId: String, text: String)` / `search(term: String): Set<String>`
- Then the reported follow-ups: AND query (`searchAll("a", "b")`), delete(docId)
- Structure ritual: what maps to what?

## Homework (not in budget)
- [ ] Intersection iterator (reported "Intersection of Two Sets" question)
- [ ] Regex-lite matcher: `.` matches any, `*` matches zero+ of preceding

## Done when
- [ ] Tests green, each committed separately
- [ ] For 3.1/3.2: explain why laziness matters (infinite/large streams, memory)

## Commit points
After each exercise.
