package br.com.mxel.exchangerates.presentation.rates.entity.mapper

import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.rates.entity.Rate
import br.com.mxel.exchangerates.presentation.rates.entity.ExchangeShow
import br.com.mxel.exchangerates.presentation.rates.entity.RateShow
import java.text.SimpleDateFormat
import java.util.*

fun exchangeToExchangeShow(exchange: Exchange): ExchangeShow = ExchangeShow(
    Currency.getInstance(exchange.base.locale).displayName,
    exchange.rates.fold(listOf()) { acc, rate ->
        // Exclude base currency from the list
        if (rate.currencyCode != exchange.base) {
            acc + rateToRateShow(
                rate,
                Rate(exchange.base, 1 / rate.amount)
            )
        } else {
            acc
        }
    },
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(exchange.date.time)
)

fun rateToRateShow(current: Rate, base: Rate): RateShow = RateShow(
    CurrencyCode.getCurrency(current.currencyCode).displayName,
    current.toString(),
    base.toString()
)
