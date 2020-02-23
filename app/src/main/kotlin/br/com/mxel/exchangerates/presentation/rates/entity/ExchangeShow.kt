package br.com.mxel.exchangerates.presentation.rates.entity

data class ExchangeShow(
    val currencyName: String,
    val rates: List<RateShow>,
    val date: String
)
