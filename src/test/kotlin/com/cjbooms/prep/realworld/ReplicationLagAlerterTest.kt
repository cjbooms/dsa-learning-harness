package com.cjbooms.prep.realworld

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class ReplicationLagAlerterTest {

    @Test
    fun `record arriving within lag does not alert`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 5)
        alerter.onPrimaryLeave("r1", 100)
        val lag = alerter.onSecondaryArrive("r1", 104)
        assertEquals(4, lag)
        assertTrue(alerter.poll(200).isEmpty())
    }

    @Test
    fun `record exceeding lag alerts exactly once`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 5)
        alerter.onPrimaryLeave("r1", 100)

        assertEquals(listOf("r1"), alerter.poll(106)) // 6 > 5
        assertTrue(alerter.poll(107).isEmpty())       // no duplicate alert
    }

    @Test
    fun `late arrival after alert is still handled`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 5)
        alerter.onPrimaryLeave("r1", 100)
        alerter.poll(106)
        assertEquals(20, alerter.onSecondaryArrive("r1", 120))
        assertEquals(0, alerter.inFlightCount)
    }

    @Test
    fun `arrival for unknown record is ignored`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 5)
        assertNull(alerter.onSecondaryArrive("ghost", 100))
    }

    @Test
    fun `multiple records alert in leave order`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 10)
        alerter.onPrimaryLeave("a", 0)
        alerter.onPrimaryLeave("b", 5)
        alerter.onPrimaryLeave("c", 20)

        assertEquals(listOf("a", "b"), alerter.poll(16)) // c has only aged -4? no: 16-20 <0 not due
        assertTrue(alerter.poll(29).isEmpty())           // 29-20=9 <= 10
        assertEquals(listOf("c"), alerter.poll(31))
    }

    @Test
    fun `arrived records do not block poll of older entries`() {
        val alerter = ReplicationLagAlerter(maxLagSeconds = 5)
        alerter.onPrimaryLeave("fast", 0)
        alerter.onPrimaryLeave("slow", 1)
        alerter.onSecondaryArrive("fast", 1)  // arrived; stale heap entry must be skipped

        assertEquals(listOf("slow"), alerter.poll(7))
    }
}
