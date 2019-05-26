package br.com.mxel.exchangerates.domain.entity

import org.junit.Assert
import org.junit.Test
import java.util.*

class CurrencyTest {

    @Test
    fun `Currency to string test`() {

        val expectedValue = "1,00 €"

        val currency = Currency("EUR", 1.0, Locale.GERMANY.toLanguageTag())

        Assert.assertEquals(expectedValue, currency.toString())
    }
}