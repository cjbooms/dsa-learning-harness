package com.cjbooms.prep.stages.stage3

/**
 * Stage 3.1 + 3.2 — sorted-stream union and k-way merge.
 * VERIFIED shape (1P3A 2026): "union iterator over sorted inputs,
 * follow-up: k iterators".
 *
 * CRITICAL requirement: LAZY. Do not materialize the inputs. The work happens
 * inside hasNext()/next(). Why laziness matters (say aloud): inputs may be
 * huge or unbounded (oplog segments, paged query results); memory stays O(1)
 * beyond the iterators themselves (O(k) for the merge).
 */

/**
 * 3.1 — Union of two sorted iterators: sorted output, no duplicates.
 * unionSorted(iter(1,3,5), iter(1,2,3)) yields 1,2,3,5
 */
fun unionSorted(a: Iterator<Int>, b: Iterator<Int>): Iterator<Int> {
    return object : Iterator<Int> {
        var headA: Int? =  if (a.hasNext()) a.next() else null
        var headB: Int? = if (b.hasNext()) b.next() else null
        override fun next(): Int {
            var value: Int?
            if (headA != null) {
                if (headB == null || headA!! < headB!!) {
                    value = headA
                    headA = if (a.hasNext()) a.next() else null
                } else if (headA == headB) {
                    value = headA
                    headA = if (a.hasNext()) a.next() else null
                    headB = if (b.hasNext()) b.next() else null
                } else {
                    value = headB
                    headB = if (b.hasNext()) b.next() else null
                }
            } else {
                value = headB
                headB = if (b.hasNext()) b.next() else null
            }
            return value ?: throw NoSuchElementException()
        }

        override fun hasNext(): Boolean {
            return headA != null || headB != null
        }
    }
}

/**
 * 3.2 — Merge K sorted iterators. O(log k) per emitted element.
 * Structure ritual: what holds the current head of each iterator?
 * (You've used this structure before — ReplicationLagAlerter's age index.)
 */
fun mergeKSorted(iterators: List<Iterator<Int>>): Iterator<Int> {
    TODO("min-structure of (head, sourceIterator); pop -> emit -> advance that source")
}

/** Tiny helper if you want it: iterator from varargs. */
fun <T> iterOf(vararg items: T): Iterator<Int> =
    items.map { it as Int }.iterator()
