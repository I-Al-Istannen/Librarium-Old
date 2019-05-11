package de.librarium.common

import java.util.*

/**
 * Converts a display name of a language to a [Locale].
 *
 * @param sourceLanguage the source language
 * @retun the found [Locale] or a new one with the input as the name
 */
fun String.toLocale(sourceLanguage: Locale = Locale.ROOT): Locale {
    return Locale.getAvailableLocales().firstOrNull {
        it.getDisplayName(sourceLanguage).equals(
            this,
            ignoreCase = true
        )
    }
        ?: Locale(this)
}
