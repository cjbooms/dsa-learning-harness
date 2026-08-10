package com.cjbooms.prep.stages.stage3

/**
 * Stage 3.3 — JSON parser (VERIFIED Senior Staff question, Nov 2025).
 * Budget 35 min — this is the real reported question, take the time.
 *
 * parse(json) builds a JsonValue tree. Decide BEFORE coding (say aloud):
 *   - tokenizer-first, or single-pass with an index cursor? (either is fine;
 *     pick the one you can narrate)
 *   - error behavior on malformed input: exception with position? sealed
 *     error type? (interviewers probe this)
 *
 * Grammar (JSON): value = object | array | string | number | true | false | null
 * Recursive descent: one function per grammar rule, each consuming exactly
 * what it parses.
 */

sealed class JsonValue {
    data class JsonObject(val entries: Map<String, JsonValue>) : JsonValue()
    data class JsonArray(val items: List<JsonValue>) : JsonValue()
    data class JsonString(val value: String) : JsonValue()
    data class JsonNumber(val value: Double) : JsonValue()
    data object JsonTrue : JsonValue()
    data object JsonFalse : JsonValue()
    data object JsonNull : JsonValue()
}

fun parse(json: String): JsonValue {
    TODO("recursive descent. Whitespace handling: decide where it's skipped.")
}
