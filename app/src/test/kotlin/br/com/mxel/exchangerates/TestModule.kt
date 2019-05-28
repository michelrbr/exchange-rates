package br.com.mxel.exchangerates

import androidx.lifecycle.LifecycleOwner
import br.com.mxel.exchangerates.data.ExchangeRepository
import br.com.mxel.exchangerates.data.remote.API_BASE_PATH
import br.com.mxel.exchangerates.data.remote.ApiClient
import br.com.mxel.exchangerates.data.remote.ExchangeTestInterceptor
import br.com.mxel.exchangerates.data.remote.RemoteClientFactory
import br.com.mxel.exchangerates.domain.ExchangeDataSource
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.usecase.GetExchangeRatesPeriodically
import br.com.mxel.exchangerates.presentation.ExchangeViewModel
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.create
import org.koin.experimental.builder.singleBy

val testModule = module {

    viewModel { (lifecycleOwner: LifecycleOwner) -> ExchangeViewModel(lifecycleOwner, get(), get()) }

    single { create<GetExchangeRatesPeriodically>() }

    singleBy<ExchangeDataSource, ExchangeRepository>()

    single {
        RemoteClientFactory(
            API_BASE_PATH,
            ExchangeTestInterceptor(),
            true
        ).createClient(ApiClient::class)
    }

    single { create<SchedulerProvider>() }

    single { TestScheduler() } bind Scheduler::class
}