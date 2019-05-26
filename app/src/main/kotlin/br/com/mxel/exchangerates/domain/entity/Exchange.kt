package br.com.mxel.exchangerates.domain.entity

data class Exchange(
    val base: String,
    val rates: Rates,
    val date: String
)