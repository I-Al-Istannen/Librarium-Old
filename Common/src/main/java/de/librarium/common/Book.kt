package de.librarium.common

import java.util.*

/**
 * The core book. Contains relevant information about a single book and nothing more.
 */
data class Book(
    val id: UUID,
    val authors: List<Author>,
    val title: String,
    val description: String,
    val isbn: String,
    val language: Locale,
    val pages: Int,
    val edition: String
) {

    /**
     * Converts this book to an unspecified "pretty" human-readable format string.
     *
     * @return this book in an unspecified "pretty" human-readable format
     */
    fun toPrettyString(): String {
        return """
            ID          : $id
            Authors     : $authors
            Title       : $title
            ISBN        : $isbn
            Language    : $language
            Pages       : $pages
            Edition     : $edition
            Description : $description
        """.trimIndent()
    }
}