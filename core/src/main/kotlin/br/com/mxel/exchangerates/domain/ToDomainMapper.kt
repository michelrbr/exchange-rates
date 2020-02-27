package br.com.mxel.exchangerates.domain

interface ToDomainMapper<T> {
    fun toDomain(): T
}