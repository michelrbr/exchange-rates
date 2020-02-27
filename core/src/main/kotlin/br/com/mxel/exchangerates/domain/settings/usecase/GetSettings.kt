package br.com.mxel.exchangerates.domain.settings.usecase

import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.settings.SettingsDataSource
import io.reactivex.Observable

class GetSettings(
    private val dataSource: SettingsDataSource
) {

    fun execute(): Observable<State<Exchange>> {

        return dataSource.getSettings()
    }
}