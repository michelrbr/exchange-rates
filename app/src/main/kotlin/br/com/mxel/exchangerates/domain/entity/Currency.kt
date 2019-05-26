package br.com.mxel.exchangerates.domain.entity

import java.text.NumberFormat
import java.util.*

data class Currency(
    val id: String,
    val value: Double,
    val languageTag: String
) {
    override fun toString(): String {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag(languageTag)).format(value)
    }
}