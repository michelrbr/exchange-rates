package br.com.mxel.exchangerates.domain.entity

import java.util.*

data class Exchange(
    val base: CurrencyCode,
    val rates: List<Rate>,
    val date: Date
)