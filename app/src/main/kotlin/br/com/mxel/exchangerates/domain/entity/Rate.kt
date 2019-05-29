package br.com.mxel.exchangerates.domain.entity

import java.text.NumberFormat

data class Rate(
    val currencyCode: CurrencyCode,
    val amount: Double = 1.0
) {
    override fun toString(): String {
        return NumberFormat.getCurrencyInstance(currencyCode.locale).format(amount)
    }
}