package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MedianFinderTest {

    @Test
    fun `single element is its own median`() {
        val mf = MedianFinder()
        mf.addNum(5)
        assertEquals(5.0, mf.findMedian(), 0.0001)
    }

    @Test
    fun `even count median is average of two middle values`() {
        val mf = MedianFinder()
        mf.addNum(1)
        mf.addNum(2)
        mf.addNum(3)
        mf.addNum(4)
        assertEquals(2.5, mf.findMedian(), 0.0001)
    }

    @Test
    fun `odd count median is the middle value`() {
        val mf = MedianFinder()
        mf.addNum(1)
        mf.addNum(2)
        mf.addNum(3)
        assertEquals(2.0, mf.findMedian(), 0.0001)
    }

    @Test
    fun `interleaved large and small values keep halves balanced`() {
        val mf = MedianFinder()
        mf.addNum(5)
        mf.addNum(2)
        mf.addNum(10)
        mf.addNum(1)
        assertEquals(3.5, mf.findMedian(), 0.0001)
    }
}
