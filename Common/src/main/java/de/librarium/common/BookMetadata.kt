package de.librarium.common

import java.time.LocalDateTime

/**
 * Stores metadata about a book and the fetcher used to fetch it.
 *
 * @property book the book itself
 * @property imageUrl the url for the cover image
 * @property fetchedUrl the url the book was fetched from
 * @property fetcherId the id of the fetcher used to download the book
 * @property fetchTime the time the book was fetched
 */
data class BookMetadata(
    val book: Book,
    val imageUrl: String,
    val fetchedUrl: String,
    val fetcherId: String,
    val fetchTime: LocalDateTime
)