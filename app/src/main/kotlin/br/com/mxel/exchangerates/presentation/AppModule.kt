package br.com.mxel.exchangerates.presentation

import androidx.lifecycle.LifecycleOwner
import br.com.mxel.exchangerates.BuildConfig
import br.com.mxel.exchangerates.data.ExchangeRepository
import br.com.mxel.exchangerates.data.remote.API_BASE_PATH
import br.com.mxel.exchangerates.data.remote.ApiClient
import br.com.mxel.exchangerates.data.remote.RemoteClientFactory
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.usecase.GetExchangeRatesPeriodically
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.create
import org.koin.experimental.builder.singleBy

val appModule = module {

    // Create ExchangeViewModel to app
    viewModel { (lifecycleOwner: LifecycleOwner) -> ExchangeViewModel(lifecycleOwner, get(), get()) }

    // Create GetExchangeRatesPeriodically to ExchangeViewModel
    single { create<GetExchangeRatesPeriodically>() }

    // Create ExchangeDataSource to GetExchangeRatesPeriodically
    singleBy<ExchangeDataSource, ExchangeRepository>()

    // Create api client through factory to ExchangeRepository
    single { RemoteClientFactory(API_BASE_PATH, null, BuildConfig.DEBUG).createClient(ApiClient::class) }

    // Create schedule provider to app
    single { SchedulerProvider(AndroidSchedulers.mainThread(), Schedulers.io()) }
}