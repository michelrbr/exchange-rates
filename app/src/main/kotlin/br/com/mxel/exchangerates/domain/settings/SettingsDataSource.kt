package br.com.mxel.exchangerates.domain.settings

import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import io.reactivex.Completable
import io.reactivex.Observable

interface SettingsDataSource {

    fun getSettings(): Observable<State<Exchange>>

    fun setSettings(exchange: Exchange): Completable
}