package com.cjbooms.prep.stages.stage12

/**
 * Multithreaded web crawler.
 *
 * Given a starting URL and a fetcher that returns the outgoing links of a
 * given URL, crawl the reachable URL graph concurrently using a fixed worker
 * pool and return every URL that can be reached from the start.
 *
 * Constructor parameters:
 *  - [fetcher]: a function that, given a URL, returns the outgoing links
 *    reachable from that URL.
 *  - [threadCount]: the number of worker threads used to fetch pages.
 *
 * Behavior:
 *  - Each URL is fetched at most once, even if it is linked from multiple
 *    pages.
 *  - Crawling stops once no more reachable, unfetched URLs remain.
 */
class WebCrawler(
    private val fetcher: (String) -> List<String>,
    private val threadCount: Int = 4,
) {

    /**
     * Start at [startUrl], fetch pages concurrently, and return every URL
     * reachable from it.
     *
     * @param startUrl the URL to begin crawling from.
     * @return the set of all reachable URLs.
     */
    fun crawl(startUrl: String): List<String> {
        TODO("implement")
    }
}
