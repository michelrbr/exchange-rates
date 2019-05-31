package br.com.mxel.exchangerates.data.remote.response

import br.com.mxel.exchangerates.domain.ToDomainMapper
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Exchange
import java.text.SimpleDateFormat
import java.util.*

data class ExchangeApi(
    val base: CurrencyCode,
    val rates: RatesApi,
    val date: String
) : ToDomainMapper<Exchange> {
    
    override fun toDomain() = Exchange(
        base,
        rates.toDomain(),
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
    )
}
