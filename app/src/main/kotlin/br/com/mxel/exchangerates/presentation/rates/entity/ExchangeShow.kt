package br.com.mxel.exchangerates.presentation.rates.entity

import br.com.mxel.exchangerates.domain.FromDomainMapper
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import java.text.SimpleDateFormat
import java.util.*

data class ExchangeShow(
    val currencyName: String,
    val rates: List<RateShow>,
    val date: String
) {
    companion object : FromDomainMapper<Exchange, ExchangeShow> {
        override fun fromDomain(from: Exchange): ExchangeShow {
            return ExchangeShow(
                Currency.getInstance(from.base.locale).displayName,
                from.rates.fold(listOf()) { acc, rate ->
                    // Exclude base currency from the list
                    if (rate.currencyCode != from.base)  acc + RateShow.fromDomain(rate) else acc
                },
                SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(from.date.time)
            )
        }

    }
}