package de.ialistannen.librarium.common.fetching.goodreads

import de.ialistannen.librarium.common.Author
import de.ialistannen.librarium.common.Book
import de.ialistannen.librarium.common.BookMetadata
import de.ialistannen.librarium.common.fetching.HttpFetcher
import de.ialistannen.librarium.common.fetching.Webclient
import de.ialistannen.librarium.common.util.toLocale
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.util.*

/**
 * A [BookFetcher](de.ialistannen.librarium.common.fetching.BookFetcher) that fetches books from
 * [Goodreads](https://goodreads.com)].
 */
class GoodreadsFetcher(webclient: Webclient) : HttpFetcher(webclient) {

    private val searchUrl: String = "https://www.goodreads.com/search?q=%s"

    override fun fetch(isbn: String): BookMetadata? {
        return fetchUrl(searchUrl.format(isbn))
    }

    override fun fetchUrl(url: String): BookMetadata? {
        val document = webclient.get(url)

        if (!isBookPage(document))
            return null

        val bookElement =
            document.getElementsByAttributeValue("itemtype", "http://schema.org/Book").first()

        val title = bookElement.getElementById("bookTitle").text()
        val isbn = getFromItemProp(bookElement, "isbn") { it }
        val authors = getAuthors(bookElement)
        val description = getDescription(bookElement)
        val edition = getFromItemProp(bookElement, "bookFormat") { it }
        val pages = getFromItemProp(bookElement, "numberOfPages") {
            it.replace(Regex("[^\\d]"), "").toInt()
        }
        val language = getFromItemProp(bookElement, "inLanguage") { it.toLocale() }

        val book =
            Book(UUID.randomUUID(), authors, title, description, isbn, language, pages, edition)

        val imageUrl = bookElement.getElementById("coverImage").absUrl("src")

        return BookMetadata(
            book, imageUrl, document.baseUri(), "goodreads", LocalDateTime.now()
        )

    }

    /**
     * Returns true if the document refers to a book info page.
     *
     * @return true if the document refers to a book info page.
     */
    private fun isBookPage(document: Document) = "/book/show/" in document.baseUri()

    private fun getAuthors(bookElement: Element): List<Author> {
        val authorElement = bookElement.getElementById("bookAuthors")

        return authorElement.getElementsByClass("authorName")
            .mapNotNull { it.getElementsByAttributeValue("itemprop", "name")?.first()?.text() }
            .map(::Author)
            .toList()
    }

    private fun getDescription(bookElement: Element): String {
        val descriptionContainer = bookElement.getElementById("description")

        return descriptionContainer.children()
            // button to toggle short/long text has a different id
            .filter { "freeText" in it.id() }
            // first is the short one so take the second
            .drop(1)
            .first()
            .text()
    }

    private fun <T> getFromItemProp(element: Element, name: String, mapper: (String) -> T): T {
        return mapper(element.getElementsByAttributeValue("itemprop", name).first().text())
    }

    override fun search(query: String): List<String> {
        val document = webclient.get(searchUrl.format(query))

        if (isBookPage(document)) {
            return listOf(document.baseUri())
        }

        return document.getElementsByAttributeValue("itemtype", "http://schema.org/Book")
            .mapNotNull { it.getElementsByAttributeValue("itemprop", "url").first() }
            .map { it.absUrl("href") }
    }
}