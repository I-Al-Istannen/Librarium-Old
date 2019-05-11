package de.ialistannen.librarium.common.fetching

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * A [Webclient] using JSoup.
 */
class JsoupHttpClient : Webclient {

    override fun get(url: String): Document {
        return Jsoup.connect(url).userAgent("Mozilla/5.0").get()
    }

}