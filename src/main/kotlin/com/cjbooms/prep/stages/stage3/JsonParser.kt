package com.cjbooms.prep.stages.stage3

/**
 * Stage 3.3 — JSON parser (VERIFIED Senior Staff question, Nov 2025).
 *
 * Recursive descent with a shared cursor: one function per grammar rule,
 * each consuming exactly what it parses by advancing pos. No substring
 * cutting — nesting is handled by parseObject/parseArray calling parse()
 * recursively. Errors throw with position; production would use a sealed
 * result type (say that aloud).
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

fun parse(json: String): JsonValue = Parser(json).parse()

private class Parser(val input: String) {
    var pos = 0

    fun parse(): JsonValue {
        skipWhiteSpace()
        return when (input[pos]) {
            '{' -> parseObject()
            '[' -> parseArray()
            '"' -> JsonValue.JsonString(parseString())
            't' -> { expectWord("true"); JsonValue.JsonTrue }
            'f' -> { expectWord("false"); JsonValue.JsonFalse }
            'n' -> { expectWord("null"); JsonValue.JsonNull }
            else -> parseNumber()
        }
    }

    fun parseObject(): JsonValue.JsonObject {
        expect('{')
        val entries = mutableMapOf<String, JsonValue>()
        skipWhiteSpace()
        if (input[pos] == '}') { pos++; return JsonValue.JsonObject(entries) }
        while (true) {
            skipWhiteSpace()
            val key = parseString()
            skipWhiteSpace(); expect(':')
            entries[key] = parse()              // recursion: any nested value
            skipWhiteSpace()
            if (input[pos] == ',') { pos++; continue }
            expect('}')
            return JsonValue.JsonObject(entries)
        }
    }

    fun parseArray(): JsonValue.JsonArray {
        expect('[')
        val items = mutableListOf<JsonValue>()
        skipWhiteSpace()
        if (input[pos] == ']') { pos++; return JsonValue.JsonArray(items) }
        while (true) {
            items.add(parse())
            skipWhiteSpace()
            if (input[pos] == ',') { pos++; continue }
            expect(']')
            return JsonValue.JsonArray(items)
        }
    }

    fun parseString(): String {
        expect('"')
        val start = pos
        while (input[pos] != '"') pos++
        return input.substring(start, pos).also { pos++ }  // consume closing quote
    }

    fun parseNumber(): JsonValue.JsonNumber {
        val start = pos
        while (pos < input.length && (input[pos].isDigit() || input[pos] in "+-.eE")) pos++
        return JsonValue.JsonNumber(input.substring(start, pos).toDouble())
    }

    fun skipWhiteSpace() {
        while (pos < input.length && input[pos].isWhitespace()) pos++
    }

    fun expect(c: Char) {
        if (input[pos] != c) error("expected '$c' at position $pos") else pos++
    }

    fun expectWord(word: String) {
        if (!input.startsWith(word, pos)) error("expected '$word' at position $pos")
        pos += word.length
    }
}
