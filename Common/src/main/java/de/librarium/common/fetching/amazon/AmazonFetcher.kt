package de.librarium.common.fetching.amazon

import de.librarium.common.Author
import de.librarium.common.Book
import de.librarium.common.BookMetadata
import de.librarium.common.fetching.HttpFetcher
import de.librarium.common.fetching.Webclient
import de.librarium.common.util.toLocale
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.util.*

/**
 * A fetcher using information from amazon.
 */
class AmazonFetcher(webclient: Webclient) : HttpFetcher(webclient) {

    private val searchUrl: String = "https://www.amazon.de/s?k=%s&i=stripbooks"

    override fun fetch(isbn: String): BookMetadata? {
        return search(isbn).firstOrNull()?.let { fetchUrl(it) }
    }

    override fun fetchUrl(url: String): BookMetadata? {
        val document = webclient.get(url)

        if (!isBookPage(document)) {
            return null
        }

        val title = document.getElementById("productTitle").text()
        val isbn = getFromDetails(
            document,
            { it.contains("ISBN-13") },
            { it.split(":").last().trim() }
        )
        val authors = getAuthors(document)
        val description = getDescription(document)
        val edition = getFromDetails(document, { true }, { it.split(":").first().trim() })
        val pages = getFromDetails(document, {
            "Seiten" in it
        }, {
            it.split(":").last().trim().replace("[^\\d]".toRegex(), "").toInt()
        })
        val language = getFromDetails(document, {
            "Sprache" in it
        }, {
            it.split(":").last().trim().toLocale(Locale.GERMAN)
        })

        val book =
            Book(UUID.randomUUID(), authors, title, description, isbn, language, pages, edition)

        val imageUrl = getImageUrl(document)

        return BookMetadata(
            book, imageUrl, document.baseUri(), "amazon", LocalDateTime.now()
        )
    }

    private fun getAuthors(element: Element): List<Author> {
        return element.getElementsByClass("contributorNameID")
            .map { it.text() }
            .map(::Author)
    }

    private fun getDescription(document: Document): String {
        return document.getElementById("bookDescription_feature_div")
            .getElementsByTag("noscript")
            .first()
            .text()
    }

    private fun <T> getFromDetails(
        mainElement: Element,
        filter: (String) -> Boolean,
        mapper: (String) -> T
    ): T {
        val listBase = mainElement.getElementById("detail_bullets_id")

        for (listItem in listBase.getElementsByTag("li")) {
            if (filter(listItem.text())) {
                return mapper(listItem.text())
            }
        }
        throw RuntimeException("could not find some detail")
    }

    private fun getImageUrl(document: Document): String {
        return document.allElements.asSequence()
            .filter {
                "imageGalleryData" in it.html()
            }
            .filter {
                it.tagName() == "script"
            }.mapNotNull {
                val matchResult =
                    Regex("""imageGalleryData.+"mainUrl":"(.+?)"""").find(it.html())

                matchResult?.groupValues?.get(1)
            }
            .first()
    }

    private fun isBookPage(document: Document): Boolean {
        return document.getElementById("booksTitle") != null
    }

    override fun search(query: String): List<String> {
        val document = webclient.get(searchUrl.format(query))

        if (isBookPage(document)) {
            return listOf(document.baseUri())
        }

        val resultList = document.getElementsByClass("s-result-list").first()

        return resultList.children().asSequence()
            .flatMap {
                it.getElementsByClass("a-link-normal").asSequence()
                    .filter { link ->
                        "/dp/" in link.attr("href")
                    }
                    .map { link -> link.absUrl("href") }
                    .take(1)
            }
            .toList()
    }

}