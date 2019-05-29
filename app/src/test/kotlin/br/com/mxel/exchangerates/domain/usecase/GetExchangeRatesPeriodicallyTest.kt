package br.com.mxel.exchangerates.domain.usecase

import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Exchange
import br.com.mxel.exchangerates.domain.entity.Rate
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class GetExchangeRatesPeriodicallyTest : BaseTest() {

    @RelaxedMockK
    lateinit var dataSource: ExchangeDataSource

    @InjectMockKs
    lateinit var useCase: GetExchangeRatesPeriodically

    private val scheduler = TestScheduler()

    @Test
    fun `Should get rates periodically`() {

        val usd = Rate(CurrencyCode.USD, 1.1187)
        val pln = Rate(CurrencyCode.PLN, 4.2974)

        val expectedValue = Exchange(
            CurrencyCode.EUR,
            listOf(
                usd,
                pln
            ),
            "2019-05-26"
        )

        every { dataSource.fetchCurrencyExchange() } returns
                Observable.just(State.data(expectedValue))

        val observable = useCase.execute(1, TimeUnit.MINUTES, scheduler)
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