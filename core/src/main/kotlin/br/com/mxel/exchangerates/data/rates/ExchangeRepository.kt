package br.com.mxel.exchangerates.data.rates

import br.com.mxel.exchangerates.data.mapToState
import br.com.mxel.exchangerates.data.rates.remote.ApiClient
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.ExchangeDataSource
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import io.reactivex.Observable

class ExchangeRepository(private val apiClient: ApiClient) : ExchangeDataSource {

    override fun fetchExchangeRates(
        baseCurrency: CurrencyCode,
        currencies: List<CurrencyCode>?
    ): Observable<State<Exchange>> {

        return apiClient.fetchExchangeRates(
            baseCurrency.name, currencies?.joinToString(",") { it.name.toUpperCase() }
        ).mapToState().toObservable()
    }
}