package de.ialistannen.librarium.common.fetching.amazon

import de.ialistannen.librarium.common.Author
import de.ialistannen.librarium.common.Book
import de.ialistannen.librarium.common.fetching.JsoupHttpClient
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class AmazonFetcherTest {
    private lateinit var fetcher: AmazonFetcher

    @BeforeEach
    internal fun setUp() {
        fetcher = AmazonFetcher(JsoupHttpClient())
    }

    @Test
    internal fun fetchSampleByIsbn() {
        val bookMetadata = fetcher.fetch("9783426517291")!!

        val expectedBook = Book(
            UUID(0, 0),
            listOf(Author("Charlotte Roth")),
            "Weil sie das Leben liebten: Roman",
            "Berlin Ende der 1920er Jahre: Die junge Franka hat nur einen Wunsch – sie möchte Zoologie studieren. Ihre strengen Eltern und die Weltwirtschaftskrise machen ihren Traum zunichte, doch immerhin gelingt es ihr, eine Stelle als Tierpflegerin im Berliner Zoo zu bekommen. Die Arbeit mit den geliebten Tieren geht ihr über alles, ihnen schenkt sie ihre ganze Liebe – nicht den Menschen. Nur ganz allmählich fasst sie Zutrauen zu dem Tierarzt Carl, der vom Leben ähnlich gebeutelt wurde wie sie. Dann lernt sie den faszinierenden Adam kennen und lieben. Doch Adam ist Sinti, und inzwischen haben die Nazis die Macht in Deutschland ergriffen. Adams Leben ist in höchster Gefahr, und Franka ist bereit, für ihn zu kämpfen – und für ihre Tiere. Fortan weiß sie nicht mehr, wem sie trauen kann …",
            "9783426517291",
            Locale.GERMAN,
            512,
            "Broschiert"
        )

        assertEquals(
            expectedBook,
            bookMetadata.book
        )

        assertEquals(
            "amazon",
            bookMetadata.fetcherId
        )
        assertEquals(
            "https://www.amazon.de/Weil-sie-das-Leben-liebten/dp/3426517299",
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
            listOf("https://www.amazon.de/Weil-sie-das-Leben-liebten/dp/3426517299"),
            search
        )
    }

    @Test
    internal fun searchByName() {
        val search = fetcher.search("Weil sie das Leben liebten")

        assertThat(
            search,
            hasItem(
                "https://www.amazon.de/Weil-sie-das-Leben-liebten/dp/3426517299"
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