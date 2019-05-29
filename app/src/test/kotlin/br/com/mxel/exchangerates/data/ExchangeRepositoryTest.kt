package br.com.mxel.exchangerates.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Exchange
import br.com.mxel.exchangerates.domain.entity.Rate
import br.com.mxel.exchangerates.testModule
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class ExchangeRepositoryTest : BaseTest(), KoinTest {

    private val repository: ExchangeDataSource by inject()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    override fun initialize() {
        startKoin { modules(testModule) }
    }

    override fun finish() = stopKoin()

    @Test
    fun `Exchange repository mapper test`() {

        val usd = Rate(CurrencyCode.USD, 1.1187)
        val pln = Rate(CurrencyCode.PLN, 4.2974)

        val expectedValue = Exchange(
            CurrencyCode.EUR,
            listOf(usd, pln),
            "2019-05-24"
        )

        repository.fetchCurrencyExchange()
            .test()
            .assertValue { state ->
                (state as? State.Data)?.let { data ->
                    data.data.base == expectedValue.base
                            && data.data.rates.find { it.currencyCode == CurrencyCode.PLN }?.toString() == pln.toString()
                            && data.data.rates.find { it.currencyCode == CurrencyCode.USD }?.toString() == usd.toString()
                            && data.data.date == expectedValue.date
                } ?: false
            }
    }
}