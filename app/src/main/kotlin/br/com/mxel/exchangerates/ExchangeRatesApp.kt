package br.com.mxel.exchangerates

import android.app.Application
import br.com.mxel.exchangerates.presentation.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class ExchangeRatesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Timber if it's a debug build
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Start dependency inject to resolve app dependencies
        startKoin {
            androidLogger()
            androidContext(this@ExchangeRatesApp)
            modules(appModule)
        }
    }
}