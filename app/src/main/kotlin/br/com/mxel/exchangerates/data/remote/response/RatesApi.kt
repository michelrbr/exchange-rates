package br.com.mxel.exchangerates.data.remote.response

import br.com.mxel.exchangerates.domain.DomainMapper
import br.com.mxel.exchangerates.domain.entity.Currency
import br.com.mxel.exchangerates.domain.entity.Rates
import com.squareup.moshi.Json
import java.util.*

data class RatesApi(
    @Json(name = "USD")
    val usd: Double,
    @Json(name = "PLN")
    val pnl: Double
) : DomainMapper<Rates> {

    override fun toDomain() = Rates(
        Currency(Identifier.USD.id, usd, Identifier.USD.lagTag),
        Currency(Identifier.PLN.id, pnl, Identifier.PLN.lagTag)
    )

    enum class Identifier(val id: String, val lagTag: String) {
        USD("USD", Locale.US.toLanguageTag()),
        PLN("PLN", "pl-PL")
    }
}