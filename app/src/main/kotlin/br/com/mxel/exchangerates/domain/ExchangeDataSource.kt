package br.com.mxel.exchangerates.domain

import br.com.mxel.exchangerates.domain.entity.Exchange
import io.reactivex.Observable

interface ExchangeDataSource {

    fun fetchCurrencyExchange(): Observable<State<Exchange>>
}