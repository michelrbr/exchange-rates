package br.com.mxel.exchangerates.domain.entity

data class Exchange(
    val base: CurrencyCode,
    val rates: List<Rate>,
    val date: String
)