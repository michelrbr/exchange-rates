package br.com.mxel.exchangerates.domain.rates.usecase

import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.ExchangeDataSource
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.rates.entity.Rate
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GetExchangeRatesPeriodicallyTest : BaseTest() {

    @RelaxedMockK
    lateinit var dataSource: ExchangeDataSource

    @InjectMockKs
    lateinit var useCase: GetExchangeRatesPeriodically

    private val scheduler = TestScheduler()

    @Test
    fun `Should get rates periodically`() {

        val usd = Rate(CurrencyCode.USD, 1.1192)
        val pln = Rate(CurrencyCode.PLN, 4.2951)

        val expectedValue = Exchange(
            CurrencyCode.EUR,
            listOf(usd, pln),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2019-05-28")
        )

        every { dataSource.fetchExchangeRates(CurrencyCode.EUR) } returns
                Observable.just(State.data(expectedValue))

        val observable = useCase.execute(CurrencyCode.EUR, null,1, TimeUnit.MINUTES, scheduler)
            .subscribeOn(scheduler)
            .observeOn(scheduler)
            .test()

        observable.assertValueCount(0)

        scheduler.advanceTimeBy(0, TimeUnit.MINUTES)
        observable.assertValueCount(2)

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
        observable.assertValueCount(4)

        observable
            .assertValueAt(0) { it is State.Loading }
            .assertValueAt(1) { it is State.Data }
            .assertValueAt(2) { it is State.Loading }
            .assertValueAt(3) { it is State.Data }

        observable.dispose()
    }
}