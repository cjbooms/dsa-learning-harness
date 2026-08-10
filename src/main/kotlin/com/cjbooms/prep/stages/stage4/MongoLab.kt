package com.cjbooms.prep.stages.stage4

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ReadConcern
import com.mongodb.WriteConcern
import com.mongodb.kotlin.client.MongoClient
import org.bson.Document

/**
 * Stage 4 lab driver — hands-on replica-set experiments.
 *
 * Setup (from repo root):
 *   docker compose up -d
 *   docker exec mongo1 mongosh --quiet --eval \
 *     'rs.initiate({_id:"rs0",members:[{_id:0,host:"mongo1:27017"},{_id:1,host:"mongo2:27017"},{_id:2,host:"mongo3:27017"}]})'
 *
 * The replica set lives INSIDE the docker network (mongo1/2/3 hostnames), so
 * this lab runs against a single node's published port and lets the driver
 * discover the topology. Connection string:
 *   mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0
 *
 * Labs (see docs/stages/04-mongodb-fluency.md):
 *   1. w:1 vs w:majority latency
 *   2. change streams — tail the oplog from Kotlin
 *   3. kill the primary mid-write (docker kill mongo1) and observe
 *
 * Record ALL observations in docs/lab-notes.md — they are your talking points.
 */

private const val CONNECTION_STRING =
    "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0&serverSelectionTimeoutMS=3000"

fun main() {
    MongoClient.create(CONNECTION_STRING).use { client ->
        val db = client.getDatabase("lab")
        val coll = db.getCollection<Document>("events")

        // ---------- LAB 1: write concern latency ----------
        // TODO: time 1000 inserts with WriteConcern.ACKNOWLEDGED (w:1)
        //       vs WriteConcern.MAJORITY. Print both totals.
        //       Hint: coll.withWriteConcern(...).insertOne(doc)
        timeInserts(coll, WriteConcern.ACKNOWLEDGED, "w:1")
        timeInserts(coll, WriteConcern.MAJORITY, "w:majority")

        // ---------- LAB 2: change stream ----------
        // TODO: open coll.watch(), then insert 5 docs from here, and print
        //       each change event's operationType + fullDocument.
        //       Recognize aloud: this IS the oplog, tailed. CDC for real.

        // ---------- LAB 3: kill the primary ----------
        // TODO: loop inserts (w:1) printing progress; while it runs,
        //       `docker kill <primary>` in another terminal.
        //       Observe: how long until inserts succeed again?
        //       Then re-run with w:majority and compare what was lost.
    }
}

private fun timeInserts(
    coll: com.mongodb.kotlin.client.MongoCollection<Document>,
    concern: WriteConcern,
    label: String,
) {
    val started = System.nanoTime()
    // TODO: insert 1000 small docs under `concern`
    val elapsedMs = (System.nanoTime() - started) / 1_000_000
    println("$label: ${elapsedMs}ms (implement me)")
}
