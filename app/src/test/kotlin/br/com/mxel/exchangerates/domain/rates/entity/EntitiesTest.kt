package br.com.mxel.exchangerates.domain.rates.entity

import org.junit.Assert
import org.junit.Test

class EntitiesTest {

    @Test
    fun `Rate to string test`() {

        val expectedEUR = "1,34 €"
        val expectedUSD = "\$1.34"
        val expectedPLN = "1,34 zł"
        val expectedBRL = "R\$ 1,34"

        val value = 1.34

        val rateEUR = Rate(CurrencyCode.EUR, value)
        val rateUSD = Rate(CurrencyCode.USD, value)
        val ratePLN = Rate(CurrencyCode.PLN, value)
        val rateBRL = Rate(CurrencyCode.BRL, value)

        Assert.assertEquals(expectedEUR, rateEUR.toString())
        Assert.assertEquals(expectedUSD, rateUSD.toString())
        Assert.assertEquals(expectedPLN, ratePLN.toString())
        Assert.assertEquals(expectedBRL, rateBRL.toString())
    }
}