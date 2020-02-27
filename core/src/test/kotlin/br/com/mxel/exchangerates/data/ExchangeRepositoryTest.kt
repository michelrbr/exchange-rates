package br.com.mxel.exchangerates.data

import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.data.rates.ExchangeRepository
import br.com.mxel.exchangerates.data.rates.remote.API_BASE_PATH
import br.com.mxel.exchangerates.data.rates.remote.ApiClient
import br.com.mxel.exchangerates.data.remote.ExchangeTestInterceptor
import br.com.mxel.exchangerates.data.remote.RemoteClientFactory
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.rates.entity.Rate
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRepositoryTest : BaseTest() {

    private val remoteSource = RemoteClientFactory(
        API_BASE_PATH,
        ExchangeTestInterceptor(),
        true
    ).createClient(ApiClient::class)

    @InjectMockKs
    lateinit var repository: ExchangeRepository

    @Test
    fun `Exchange repository mapper test`() {

        val list = listOf(
            Rate(CurrencyCode.AUD, 1.6162),
            Rate(CurrencyCode.BGN, 1.9558),
            Rate(CurrencyCode.BRL, 4.5305),
            Rate(CurrencyCode.CAD, 1.5071),
            Rate(CurrencyCode.CNY, 7.7345),
            Rate(CurrencyCode.CZK, 25.843),
            Rate(CurrencyCode.GBP, 0.88373),
            Rate(CurrencyCode.HKD, 8.7839),
            Rate(CurrencyCode.HUF, 326.6),
            Rate(CurrencyCode.IDR, 16088.5),
            Rate(CurrencyCode.ILS, 4.0445),
            Rate(CurrencyCode.INR, 77.9025),
            Rate(CurrencyCode.JPY, 122.45),
            Rate(CurrencyCode.DKK, 7.4689),
            Rate(CurrencyCode.KRW, 1329.89),
            Rate(CurrencyCode.MXN, 21.3797),
            Rate(CurrencyCode.MYR, 4.6878),
            Rate(CurrencyCode.NOK, 9.7165),
            Rate(CurrencyCode.NZD, 1.7088),
            Rate(CurrencyCode.PHP, 58.489),
            Rate(CurrencyCode.PLN, 4.2951),
            Rate(CurrencyCode.RUB, 72.2891),
            Rate(CurrencyCode.SEK, 10.6865),
            Rate(CurrencyCode.SGD, 1.542),
            Rate(CurrencyCode.TRY, 6.753),
            Rate(CurrencyCode.USD, 1.1192),
            Rate(CurrencyCode.ZAR, 16.3494)
        )

        val expectedValue = Exchange(
            CurrencyCode.EUR,
            list,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2019-05-28")
        )

        repository.fetchExchangeRates(CurrencyCode.EUR)
            .test()
            .assertValue { state ->
                (state as? State.Data)?.let { data ->

                    data.data.base == expectedValue.base
                            // Transform rate list into a boolean list and verify if all items is true
                            && data.data.rates.map { rate ->
                        rate == expectedValue.rates.find { it.currencyCode == rate.currencyCode }
                    }.reduce { acc, b -> acc && b }

                            && data.data.date == expectedValue.date
                } ?: false
            }
    }
}
