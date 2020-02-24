package br.com.mxel.exchangerates.presentation.rates.entity.mapper

import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.rates.entity.Rate
import br.com.mxel.exchangerates.presentation.rates.entity.ExchangeShow
import br.com.mxel.exchangerates.presentation.rates.entity.RateShow
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class RateMapperTest {

    @Test
    fun `Should transform Rate into RateShow`() {

        val expectedRateShow = RateShow("US Dollar", "$1.00", "R$ 1,00")

        val mappedRateShow = rateToRateShow(Rate(CurrencyCode.USD, 1.0), Rate(CurrencyCode.BRL, 1.0))

        Assert.assertEquals(expectedRateShow, mappedRateShow)
    }

    @Test
    fun `Should transform Exchange into ExchangeShow`() {

        val expectedExchangeShow = ExchangeShow("US Dollar", emptyList(),"24 Feb 2020")

        val mappedExchangeShow = exchangeToExchangeShow(
            Exchange(
                CurrencyCode.USD,
                emptyList(),
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2020-02-24")
            )
        )

        Assert.assertEquals(expectedExchangeShow, mappedExchangeShow)
    }
}
