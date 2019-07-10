package br.com.mxel.exchangerates

import br.com.mxel.exchangerates.data.rates.ExchangeRepository
import br.com.mxel.exchangerates.data.rates.remote.API_BASE_PATH
import br.com.mxel.exchangerates.data.rates.remote.ApiClient
import br.com.mxel.exchangerates.data.remote.ExchangeTestInterceptor
import br.com.mxel.exchangerates.data.remote.RemoteClientFactory
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.rates.ExchangeDataSource
import br.com.mxel.exchangerates.domain.rates.usecase.GetExchangeRatesPeriodically
import br.com.mxel.exchangerates.presentation.rates.ExchangeViewModel
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy

val testModule = module {

    viewModel { ExchangeViewModel(get(), get()) }

    single<GetExchangeRatesPeriodically>()

    singleBy<ExchangeDataSource, ExchangeRepository>()

    single {
        RemoteClientFactory(
            API_BASE_PATH,
            ExchangeTestInterceptor(),
            true
        ).createClient(ApiClient::class)
    }

    single<SchedulerProvider>()

    single { TestScheduler() } bind Scheduler::class
}