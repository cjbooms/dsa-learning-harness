package com.cjbooms.prep.stages.stage12

/**
 * Producer/consumer bounded blocking queue.
 *
 * Implement a FIFO queue with a fixed [capacity]. Producers block when the
 * queue is full; consumers block when the queue is empty. The queue must be
 * safe for use by many concurrent producers and many concurrent consumers.
 *
 * Constructor parameter:
 *  - [capacity]: the maximum number of items the queue can hold. MUST be
 *    positive.
 *
 * Behavior:
 *  - `put(item)` blocks the calling thread until a slot is free, then appends
 *    the item.
 *  - `take()` blocks the calling thread until an item is available, then
 *    removes and returns the head item.
 *  - `size` returns the current number of items in the queue without
 *    blocking.
 */
class BoundedBlockingQueue<T>(private val capacity: Int) {

    /**
     * Block until a slot is free, then append [item].
     *
     * @param item the value to enqueue.
     */
    fun put(item: T) {
        TODO("implement")
    }

    /**
     * Block until an item is available, then remove and return the head.
     *
     * @return the head item.
     */
    fun take(): T {
        TODO("implement")
    }
    /**
     * @return the current number of items in the queue.
     */
    val size: Int
        get() {
            TODO("implement")
        }

}
