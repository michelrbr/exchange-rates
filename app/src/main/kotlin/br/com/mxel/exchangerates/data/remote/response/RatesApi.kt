package br.com.mxel.exchangerates.data.remote.response

import br.com.mxel.exchangerates.domain.DomainMapper
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Rate
import com.squareup.moshi.Json
import kotlin.reflect.full.memberProperties

data class RatesApi(
    @Json(name = "EUR")
    val eur: Double? = null,
    @Json(name = "USD")
    val usd: Double? = null,
    @Json(name = "PLN")
    val pln: Double? = null,
    @Json(name = "BGN")
    val bgn: Double? = null,
    @Json(name = "NZD")
    val nzd: Double? = null,
    @Json(name = "ILS")
    val ils: Double? = null,
    @Json(name = "RUB")
    val rub: Double? = null,
    @Json(name = "CAD")
    val cad: Double? = null,
    @Json(name = "PHP")
    val php: Double? = null,
    @Json(name = "ZAR")
    val zar: Double? = null,
    @Json(name = "AUD")
    val aud: Double? = null,
    @Json(name = "JPY")
    val jpy: Double? = null,
    @Json(name = "TRY")
    val `try`: Double? = null,
    @Json(name = "HKD")
    val hkd: Double? = null,
    @Json(name = "MYR")
    val myr: Double? = null,
    @Json(name = "NOK")
    val nok: Double? = null,
    @Json(name = "IDR")
    val idr: Double? = null,
    @Json(name = "DKK")
    val dkk: Double? = null,
    @Json(name = "CZK")
    val czk: Double? = null,
    @Json(name = "HUF")
    val huf: Double? = null,
    @Json(name = "GBP")
    val gbp: Double? = null,
    @Json(name = "MXN")
    val mxn: Double? = null,
    @Json(name = "KRW")
    val krw: Double? = null,
    @Json(name = "SGD")
    val sgd: Double? = null,
    @Json(name = "BRL")
    val brl: Double? = null,
    @Json(name = "INR")
    val inr: Double? = null,
    @Json(name = "CNY")
    val cny: Double? = null,
    @Json(name = "SEK")
    val sek: Double? = null
) : DomainMapper<List<Rate>> {

    override fun toDomain(): List<Rate> {

        val list = ArrayList<Rate>()

        javaClass.kotlin.memberProperties.forEach {
            if (it.get(this) != null) {
                list.add(
                    Rate(
                        CurrencyCode.valueOf(it.name.toUpperCase()),
                        it.get(this) as Double
                    )
                )
            }
            it.get(this).toString()
        }

        return list
    }


}