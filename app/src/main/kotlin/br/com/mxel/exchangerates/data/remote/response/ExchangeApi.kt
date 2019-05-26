package br.com.mxel.exchangerates.data.remote.response

import br.com.mxel.exchangerates.domain.DomainMapper
import br.com.mxel.exchangerates.domain.entity.Exchange

data class ExchangeApi(
    val base: String,
    val rates: RatesApi,
    val date: String
) : DomainMapper<Exchange> {
    override fun toDomain() = Exchange(
        base,
        rates.toDomain(),
        date
    )
}
