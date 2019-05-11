package de.ialistannen.librarium.common.fetching.chained

import de.ialistannen.librarium.common.BookMetadata
import de.ialistannen.librarium.common.fetching.BookFetcher

/**
 * A fetcher that just calls multiple fetcher in order and picks the first one.
 */
class ChainedFetcher(private val fetcher: List<BookFetcher>) : BookFetcher {

    override fun fetch(isbn: String): BookMetadata? {
        return fetcher.asSequence()
            .mapNotNull { it.fetch(isbn) }
            .firstOrNull()
    }

    override fun fetchUrl(url: String): BookMetadata? {
        return fetcher.asSequence()
            .mapNotNull { it.fetchUrl(url) }
            .firstOrNull()
    }

    override fun search(query: String): List<String> {
        return fetcher.asSequence()
            .mapNotNull { it.search(query) }
            .first()
    }
}