package br.com.mxel.exchangerates.domain

import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Exchange
import io.reactivex.Observable

interface ExchangeDataSource {

    fun fetchExchangeRates(base: CurrencyCode? = null): Observable<State<Exchange>>
}