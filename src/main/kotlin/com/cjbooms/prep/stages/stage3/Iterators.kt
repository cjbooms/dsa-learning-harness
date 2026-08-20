package com.cjbooms.prep.stages.stage3

import java.util.PriorityQueue

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
    val currentHeads = mutableMapOf<Int, Int?>()
    iterators.forEachIndexed { index, iterator ->
       currentHeads[index] = if (iterator.hasNext()) iterator.next() else null
    }
    return object : Iterator<Int> {

        override fun next(): Int {
            var lowest: Int = Int.MAX_VALUE
            var lowestTracking = mutableSetOf<Int>()
            currentHeads.forEach { index, value ->
                if (value != null) {
                    if (value < lowest) {
                        lowest = value
                        lowestTracking.clear()
                        lowestTracking.add(index)
                    } else if (value == lowest) {
                        lowestTracking.add(index)
                    }
                }
            }
            lowestTracking.forEach {
                currentHeads[it] = if (iterators[it].hasNext()) iterators[it].next() else null
            }
            lowestTracking.clear()

            return if (lowest != Int.MAX_VALUE) lowest else throw NoSuchElementException()
        }

        override fun hasNext(): Boolean {
            return currentHeads.filter { it.value != null }.isNotEmpty()
        }
    }
}

fun mergeKSortedPriorityQ(iterators: List<Iterator<Int>>): Iterator<Int> {
    val queue = PriorityQueue<Pair<Int, Int>>( compareBy { it.second })
    iterators.forEachIndexed { index, iterator ->
        if (iterator.hasNext()) queue.add(index to iterator.next())
    }

    return object : Iterator<Int> {

        override fun next(): Int {
            val (index, value) = queue.poll() ?: throw NoSuchElementException()
            if (iterators[index].hasNext()) queue.add(index to iterators[index].next())
            // Check for duplicates
            var dupeChecked = false
            while (!dupeChecked) {
                if (queue.size > 0) {
                    val (nextIndex, nextValue) = queue.peek()
                    if (value == nextValue) {
                        queue.poll()
                        if (iterators[nextIndex].hasNext()) queue.add(nextIndex to iterators[nextIndex].next())
                    } else {
                        dupeChecked = true
                    }
                } else  dupeChecked = true
            }
            return value
        }

        override fun hasNext(): Boolean {
            return queue.isNotEmpty()
        }
    }
}

/** Tiny helper if you want it: iterator from varargs. */
fun <T> iterOf(vararg items: T): Iterator<Int> =
    items.map { it as Int }.iterator()
