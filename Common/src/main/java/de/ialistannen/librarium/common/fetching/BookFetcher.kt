package de.ialistannen.librarium.common.fetching

import de.ialistannen.librarium.common.Book
import de.ialistannen.librarium.common.BookMetadata

/**
 * A fetcher for [Book]s.
 */
interface BookFetcher {

    /**
     * Fetches a book with the given isbn.
     *
     * @param isbn the isbn of the book to fetch
     * @return the book and its metadata or null if not found
     */
    fun fetch(isbn: String): BookMetadata?

    /**
     * Fetches a book from the given url.
     *
     * @param url the url of the book
     * @return the book or null if not found
     */
    fun fetchUrl(url: String): BookMetadata?

    /**
     * Searches for books using a given query.
     */
    fun search(query: String): List<String>
}

/**
 * A search result.
 *
 * @property entries the list with results
 * @property fetcher the fetcher used to search
 */
data class SearchResult(val entries: List<SearchResultEntry>, val fetcher: BookFetcher)

/**
 * A search result entry, e.g. a single book.
 *
 * @property information some information about the search entry
 * @property url the url to pass to the fetcher that yielded this result
 */
data class SearchResultEntry(val information: String, val url: String)