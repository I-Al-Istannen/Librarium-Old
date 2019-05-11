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
)