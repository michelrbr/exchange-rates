package br.com.mxel.exchangerates.data

import br.com.mxel.exchangerates.data.remote.ApiClient
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Exchange
import io.reactivex.Observable

class ExchangeRepository(private val apiClient: ApiClient) : ExchangeDataSource {

    override fun fetchCurrencyExchange(): Observable<State<Exchange>> {

        return apiClient.getMoviesOrderedBy().mapToState().toObservable()
    }
}