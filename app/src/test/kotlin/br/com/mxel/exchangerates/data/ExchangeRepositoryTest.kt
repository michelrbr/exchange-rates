package br.com.mxel.exchangerates.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Currency
import br.com.mxel.exchangerates.domain.entity.Exchange
import br.com.mxel.exchangerates.domain.entity.Rates
import br.com.mxel.exchangerates.testModule
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import java.util.*

class ExchangeRepositoryTest : BaseTest(), KoinTest {

    private val repository: ExchangeDataSource by inject()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    override fun initialize() {
        StandAloneContext.startKoin(listOf(testModule))
    }

    override fun finish() = StandAloneContext.stopKoin()

    @Test
    fun `Exchange repository mapper test`() {

        val usd = Currency("USD", 1.1187, Locale.US.toLanguageTag())
        val pln = Currency("PLN", 4.2974, "pl-PL")

        val expectedValue = Exchange(
            "EUR",
            Rates(usd, pln),
            "2019-05-24"
        )

        repository.fetchCurrencyExchange()
            .test()
            .assertValue { state ->
                (state as? State.Data)?.let {
                    it.data.base == expectedValue.base
                            && it.data.rates.pln.toString() == expectedValue.rates.pln.toString()
                            && it.data.rates.usd.toString() == expectedValue.rates.usd.toString()
                            && it.data.date == expectedValue.date
                } ?: false
            }
    }
}