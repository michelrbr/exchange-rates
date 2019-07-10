package br.com.mxel.exchangerates.domain.rates.usecase

import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.ExchangeDataSource
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GetExchangeRatesPeriodically(
    private val dateSource: ExchangeDataSource
) {

    fun execute(
        baseCurrency: CurrencyCode? = null,
        currencies: List<CurrencyCode>? = null,
        time: Long = 1,
        timeUnit: TimeUnit = TimeUnit.MINUTES,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<State<Exchange>> {

        val base = baseCurrency ?: CurrencyCode.EUR

        return Observable.interval(0, time, timeUnit, scheduler)
            .flatMap {
                Observable.concat(Observable.just(State.loading()), dateSource.fetchExchangeRates(base, currencies))
            }
    }
}