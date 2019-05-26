package br.com.mxel.exchangerates.domain

interface DomainMapper<T> {
    fun toDomain(): T
}