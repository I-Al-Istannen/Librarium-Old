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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (authors != other.authors) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (isbn != other.isbn) return false
        if (language != other.language) return false
        if (pages != other.pages) return false
        if (edition != other.edition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = authors.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + isbn.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + pages
        result = 31 * result + edition.hashCode()
        return result
    }


}