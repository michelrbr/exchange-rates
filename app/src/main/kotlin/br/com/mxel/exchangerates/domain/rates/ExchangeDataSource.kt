package br.com.mxel.exchangerates.domain.rates

import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import io.reactivex.Observable

interface ExchangeDataSource {

    fun fetchExchangeRates(
        baseCurrency: CurrencyCode,
        currencies: List<CurrencyCode>? = null
    ): Observable<State<Exchange>>
}