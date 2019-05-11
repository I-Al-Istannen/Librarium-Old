package de.ialistannen.librarium.common.fetching.goodreads

import de.ialistannen.librarium.common.Author
import de.ialistannen.librarium.common.Book
import de.ialistannen.librarium.common.fetching.JsoupHttpClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class GoodreadsFetcherTest {

    private lateinit var fetcher: GoodreadsFetcher

    @BeforeEach
    internal fun setUp() {
        fetcher = GoodreadsFetcher(JsoupHttpClient())
    }

    @Test
    internal fun fetchSampleByIsbn() {
        val bookMetadata = fetcher.fetch("9783426517291")!!

        val expectedBook = Book(
            UUID(0, 0),
            listOf(Author("Charlotte Roth")),
            "Weil sie das Leben liebten",
            "Berlin Ende der 1920er Jahre: Die junge Franka hat nur einen Wunsch – sie möchte Zoologie studieren. Ihre strengen Eltern und die Weltwirtschaftskrise machen ihren Traum zunichte, doch immerhin gelingt es ihr, eine Stelle als Tierpflegerin im Berliner Zoo zu bekommen. Die Arbeit mit den geliebten Tieren geht ihr über alles, ihnen schenkt sie ihre ganze Liebe – nicht den Menschen. Nur ganz allmählich fasst sie Zutrauen zu dem Tierarzt Carl, der vom Leben ähnlich gebeutelt wurde wie sie. Dann lernt sie den faszinierenden Adam kennen und lieben. Doch Adam ist Sinti, und inzwischen haben die Nazis die Macht in Deutschland ergriffen. Adams Leben ist in höchster Gefahr, und Franka ist bereit, für ihn zu kämpfen – und für ihre Tiere. Fortan weiß sie nicht mehr, wem sie trauen kann …",
            "9783426517291",
            Locale.GERMAN,
            512,
            "Paperback"
        )

        assertEquals(
            expectedBook,
            bookMetadata.book
        )

        assertEquals(
            "goodreads",
            bookMetadata.fetcherId
        )
        assertEquals(
            "https://www.goodreads.com/book/show/30312506-weil-sie-das-leben-liebten",
            bookMetadata.fetchedUrl
        )
        assertEquals(
            "https://images.gr-assets.com/books/1464529938l/30312506.jpg",
            bookMetadata.imageUrl
        )
    }

    @Test
    internal fun searchByIsbn() {
        val search = fetcher.search("9783426517291")

        assertEquals(
            search,
            listOf("https://www.goodreads.com/book/show/30312506-weil-sie-das-leben-liebten")
        )
    }

    @Test
    internal fun searchByName() {
        val search = fetcher.search("Weil sie das Leben liebten")

        assertEquals(
            search,
            listOf(
                "https://www.goodreads.com/book/show/30312506-weil-sie-das-leben-liebten?from_search=true",
                "https://www.goodreads.com/book/show/44420860-weil-sie-das-leben-liebten?from_search=true"
            )
        )
    }

    @Test
    internal fun searchNotFound() {
        val search = fetcher.search("You will likely not find anything for this data!")

        assertEquals(
            search,
            emptyList<String>()
        )
    }

}