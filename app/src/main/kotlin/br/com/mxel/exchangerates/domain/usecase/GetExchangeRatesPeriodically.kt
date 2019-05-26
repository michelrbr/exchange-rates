package br.com.mxel.exchangerates.domain.usecase

import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Exchange
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class GetExchangeRatesPeriodically(
    private val dateSource: ExchangeDataSource
) {

    fun execute(
        time: Long = 1,
        timeUnit: TimeUnit = TimeUnit.MINUTES,
        scheduler: Scheduler
    ): Observable<State<Exchange>> {

        return Observable.interval(0, time, timeUnit, scheduler)
            .flatMap { Observable.concat(Observable.just(State.loading()), dateSource.fetchCurrencyExchange()) }
    }
}