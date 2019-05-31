package br.com.mxel.exchangerates.presentation.entity

import br.com.mxel.exchangerates.domain.FromDomainMapper
import br.com.mxel.exchangerates.domain.entity.Exchange
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
                from.rates.map { RateShow.fromDomain(it) },
                SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(from.date.time)
            )
        }

    }
}