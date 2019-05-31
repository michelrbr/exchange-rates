package br.com.mxel.exchangerates.presentation.entity

import br.com.mxel.exchangerates.domain.FromDomainMapper
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Rate

data class RateShow(
    val currencyName: String,
    val amountRep: String
) {
    companion object : FromDomainMapper<Rate, RateShow> {
        override fun fromDomain(from: Rate): RateShow {
            return RateShow(
                CurrencyCode.getCurrency(from.currencyCode).displayName,
                from.toString()
            )
        }

    }
}