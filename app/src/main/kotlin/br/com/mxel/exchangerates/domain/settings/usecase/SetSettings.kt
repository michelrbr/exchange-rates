package br.com.mxel.exchangerates.domain.settings.usecase

import br.com.mxel.exchangerates.domain.rates.entity.Exchange
import br.com.mxel.exchangerates.domain.settings.SettingsDataSource
import io.reactivex.Completable

class SetSettings(
    private val dataSource: SettingsDataSource
) {

    fun execute(exchange: Exchange): Completable {

        return dataSource.setSettings(exchange)
    }
}