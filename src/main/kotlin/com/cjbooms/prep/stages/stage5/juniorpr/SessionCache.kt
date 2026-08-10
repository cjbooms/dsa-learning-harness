package com.cjbooms.prep.stages.stage5.juniorpr

import java.io.FileWriter
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

/**
 * PR #482 — "Session cache with audit logging"
 * Author: junior colleague
 * Description: Caches user sessions in memory for fast lookup, refreshes
 * them from the database in the background, and appends an audit line to
 * a log file whenever a session is created or evicted.
 *
 * REVIEW INSTRUCTIONS (Stage 5.1): treat this as the PR under review.
 * Leave // comments as you go. Do NOT open Manifest.kt.txt until your
 * 25 minutes are up. There are at least 6 planted issues of varying severity.
 */
class SessionCache(private val auditLogPath: String) {

    private val sessions = ConcurrentHashMap<String, Session>()
    private var lastEvictionReport = ""

    data class Session(val userId: String, val token: String, val createdAt: Long) {
        var refreshCount = 0
    }

    fun getSession(userId: String): Session? {
        return sessions[userId]
    }

    fun createSession(userId: String, token: String): Session {
        val session = Session(userId, token, System.currentTimeMillis())
        sessions[userId] = session
        audit("created session for $userId")
        return session
    }

    fun evictIdleSessions(maxIdleMillis: Long) {
        val now = System.currentTimeMillis()
        for ((userId, session) in sessions) {
            if (now - session.createdAt > maxIdleMillis) {
                sessions.remove(userId)
                audit("evicted $userId")
            }
        }
        lastEvictionReport = "evicted up to $now"
    }

    fun startBackgroundRefresh(refreshIntervalMillis: Long) {
        thread(isDaemon = true) {
            while (true) {
                Thread.sleep(refreshIntervalMillis)
                for (session in sessions.values) {
                    session.refreshCount++
                }
            }
        }
    }

    private fun audit(message: String) {
        val writer = FileWriter(auditLogPath, true)
        writer.write("${System.currentTimeMillis()} $message\n")
        writer.close()
    }

    fun evictionReport(): String = lastEvictionReport
}
