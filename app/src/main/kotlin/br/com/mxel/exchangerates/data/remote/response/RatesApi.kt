package br.com.mxel.exchangerates.data.remote.response

import br.com.mxel.exchangerates.domain.DomainMapper
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Rate
import com.squareup.moshi.Json
import java.util.*

data class RatesApi(
    @Json(name = "USD")
    val usd: Double,
    @Json(name = "PLN")
    val pln: Double
) : DomainMapper<List<Rate>> {

    override fun toDomain(): List<Rate> {

        return listOf(
            Rate(CurrencyCode.USD, usd),
            Rate(CurrencyCode.PLN, pln)
        )
    }

    enum class Identifier(val langId: String) {
        USD(Locale.US.toLanguageTag()),
        PLN("pl-PL")
    }
}