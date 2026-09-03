package com.cjbooms.prep.stages.stage12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class WebCrawlerTest {

    @Test
    fun `crawls a simple chain`() {
        val graph = mapOf(
            "a" to listOf("b"),
            "b" to listOf("c"),
            "c" to emptyList(),
        )
        val crawler = WebCrawler(fetcher = { graph[it] ?: emptyList() })
        assertEquals(listOf("a", "b", "c"), crawler.crawl("a"))
    }

    @Test
    fun `handles a cycle without infinite loop`() {
        val graph = mapOf(
            "a" to listOf("b"),
            "b" to listOf("a"),
        )
        val crawler = WebCrawler(fetcher = { graph[it] ?: emptyList() })
        assertEquals(listOf("a", "b"), crawler.crawl("a"))
    }

    @Test
    fun `crawls a diamond`() {
        val graph = mapOf(
            "a" to listOf("b", "c"),
            "b" to listOf("d"),
            "c" to listOf("d"),
            "d" to emptyList(),
        )
        val crawler = WebCrawler(fetcher = { graph[it] ?: emptyList() })
        assertEquals(listOf("a", "b", "c", "d"), crawler.crawl("a"))
    }

    @Test
    fun `single page with no links`() {
        val crawler = WebCrawler(fetcher = { emptyList() })
        assertEquals(listOf("a"), crawler.crawl("a"))
    }
}
