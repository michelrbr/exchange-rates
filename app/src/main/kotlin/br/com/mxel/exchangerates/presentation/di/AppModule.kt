package br.com.mxel.exchangerates.presentation.di

import br.com.mxel.exchangerates.BuildConfig
import br.com.mxel.exchangerates.data.rates.ExchangeRepository
import br.com.mxel.exchangerates.data.rates.remote.API_BASE_PATH
import br.com.mxel.exchangerates.data.rates.remote.ApiClient
import br.com.mxel.exchangerates.data.remote.RemoteClientFactory
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.rates.ExchangeDataSource
import br.com.mxel.exchangerates.domain.rates.usecase.GetExchangeRatesPeriodically
import br.com.mxel.exchangerates.presentation.rates.ExchangeActivity
import br.com.mxel.exchangerates.presentation.rates.ExchangeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.experimental.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.scoped
import org.koin.experimental.builder.scopedBy

val appModule = module {

    scope(named<ExchangeActivity>()) {

        // Create ExchangeViewModel to ExchangeActivity
        viewModel<ExchangeViewModel>()

        // Create GetExchangeRatesPeriodically to ExchangeViewModel
        scoped<GetExchangeRatesPeriodically>()

        // Create ExchangeDataSource to GetExchangeRatesPeriodically
        scopedBy<ExchangeDataSource, ExchangeRepository>()

        // Create api client through factory to ExchangeRepository
        scoped { get<RemoteClientFactory>().createClient(ApiClient::class) }
    }

    // Create remote client factory to app
    single { RemoteClientFactory(API_BASE_PATH, null, BuildConfig.DEBUG) }

    // Create schedule provider to app
    single { SchedulerProvider(AndroidSchedulers.mainThread(), Schedulers.io()) }
}