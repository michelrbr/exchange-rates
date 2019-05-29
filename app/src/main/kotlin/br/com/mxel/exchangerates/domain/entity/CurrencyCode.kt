package br.com.mxel.exchangerates.domain.entity

import java.util.*

enum class CurrencyCode(val locale: Locale) {
    EUR(Locale.GERMANY),
    USD(Locale.US),
    PLN(Locale("pl", "PL")),
    BGN(Locale("bg", "BG")),
    NZD(Locale("en", "NZ")),
    ILS(Locale("iw", "IL")),
    RUB(Locale("ru", "RU")),
    CAD(Locale.CANADA),
    PHP(Locale("en", "PH")),
    //CHF(Locale("fr", "CH")),
    ZAR(Locale("en", "ZA")),
    AUD(Locale("en", "AU")),
    JPY(Locale.JAPAN),
    TRY(Locale("tr", "TR")),
    HKD(Locale("zh", "HK")),
    MYR(Locale("ms", "MY")),
    //THB(Locale("th","TH")),
    //HRK(Locale("hr","HR")),
    NOK(Locale("no", "NO")),
    IDR(Locale("in", "ID")),
    KK(Locale("da", "DK")),
    CZK(Locale("cs", "CZ")),
    HUF(Locale("hu", "HU")),
    GBP(Locale.UK),
    MXN(Locale("es", "MX")),
    KRW(Locale("ko", "KR")),
    //ISK(Locale("is","IS")),
    SGD(Locale("en", "SG")),
    BRL(Locale("pt", "BR")),
    INR(Locale("hi", "IN")),
    //RON(Locale("ro", "RO")),
    CNY(Locale.CHINA),
    SEK(Locale("sv", "SE"));

    companion object {
        fun getCurrency(identifier: CurrencyCode): Currency {
            return Currency.getInstance(identifier.locale)
        }
    }
}