package de.librarium.common.fetching

import org.jsoup.nodes.Document

/**
 * The base class for http fetchers.
 */
abstract class HttpFetcher(protected val webclient: Webclient) : BookFetcher

/**
 * A simple web client abstraction to execute GET requests.
 */
interface Webclient {

    /**
     * Fetches a website with the given url.
     *
     * @param url the url
     * @throws WebsiteFetchException if an error occurred
     */
    fun get(url: String): Document
}

/**
 * An exception indicating something went wrong while fetching a webpage.
 */
class WebsiteFetchException(message: String, cause: Throwable? = null) : Exception(message, cause)