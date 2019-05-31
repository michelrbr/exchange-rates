package br.com.mxel.exchangerates.domain

interface FromDomainMapper<F, T> {
    fun fromDomain(from: F): T
}