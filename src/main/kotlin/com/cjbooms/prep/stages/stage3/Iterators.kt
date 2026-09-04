package com.cjbooms.prep.stages.stage3

import java.util.PriorityQueue

/**
 * Returns a sorted iterator that yields the elements of the sorted iterators
 * [a] and [b] in ascending order, with duplicates appearing only once.
 *
 * @param a the first sorted input iterator
 * @param b the second sorted input iterator
 * @return a lazy iterator over the sorted union of [a] and [b] with duplicates
 *   removed
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
 * Returns a lazy iterator that merges [iterators] into a single ascending
 * sequence. Each input iterator must already be sorted in ascending order.
 * The merge is lazy: nothing is materialized beyond what the iterators
 * themselves expose (O(k) state for k iterators).
 *
 * @param iterators the sorted input iterators to merge
 * @return a lazy iterator over the merged ascending sequence
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

/**
 * Returns a lazy iterator that merges [iterators] using a priority queue,
 * emitting each next element in O(log k) time where k is the number of input
 * iterators. Each input iterator must already be sorted in ascending order.
 *
 * @param iterators the sorted input iterators to merge
 * @return a lazy iterator over the merged ascending sequence
 */
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

/**
 * Returns an iterator over the supplied [items].
 *
 * @param items the elements to iterate over
 * @return an [Iterator] yielding [items] in order
 */
fun <T> iterOf(vararg items: T): Iterator<Int> =
    items.map { it as Int }.iterator()
