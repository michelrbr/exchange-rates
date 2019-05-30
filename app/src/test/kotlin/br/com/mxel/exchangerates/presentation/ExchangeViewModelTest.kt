package br.com.mxel.exchangerates.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Rate
import br.com.mxel.exchangerates.testModule
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifySequence
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit

class ExchangeViewModelTest : BaseTest(), KoinTest {

    private val viewModel: ExchangeViewModel by inject { parametersOf(lifecycleOwner) }

    private val scheduler: TestScheduler by inject()

    @RelaxedMockK
    lateinit var lifecycleOwner: LifecycleOwner

    @RelaxedMockK
    lateinit var loadingObserver: Observer<Boolean>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    override fun initialize() {
        startKoin { modules(testModule) }
    }

    override fun finish() = stopKoin()

    @Test
    fun `Exchange view model test`() {

        val list = listOf(
            Rate(CurrencyCode.AUD, 1.6162),
            Rate(CurrencyCode.BGN, 1.9558),
            Rate(CurrencyCode.BRL, 4.5305),
            Rate(CurrencyCode.CAD, 1.5071),
            Rate(CurrencyCode.CNY, 7.7345),
            Rate(CurrencyCode.CZK, 25.843),
            Rate(CurrencyCode.GBP, 0.88373),
            Rate(CurrencyCode.HKD, 8.7839),
            Rate(CurrencyCode.HUF, 326.6),
            Rate(CurrencyCode.IDR, 16088.5),
            Rate(CurrencyCode.ILS, 4.0445),
            Rate(CurrencyCode.INR, 77.9025),
            Rate(CurrencyCode.JPY, 122.45),
            Rate(CurrencyCode.DKK, 7.4689),
            Rate(CurrencyCode.KRW, 1329.89),
            Rate(CurrencyCode.MXN, 21.3797),
            Rate(CurrencyCode.MYR, 4.6878),
            Rate(CurrencyCode.NOK, 9.7165),
            Rate(CurrencyCode.NZD, 1.7088),
            Rate(CurrencyCode.PHP, 58.489),
            Rate(CurrencyCode.PLN, 4.2951),
            Rate(CurrencyCode.RUB, 72.2891),
            Rate(CurrencyCode.SEK, 10.6865),
            Rate(CurrencyCode.SGD, 1.542),
            Rate(CurrencyCode.TRY, 6.753),
            Rate(CurrencyCode.USD, 1.1192),
            Rate(CurrencyCode.ZAR, 16.3494)
        )

        val lifecycle = LifecycleRegistry(lifecycleOwner).apply {
            // Need to add viewModel as a lifecycle observable, looks like mocked lifecycleOwner doesn't work properly
            addObserver(viewModel)
        }

        viewModel.loading.observeForever(loadingObserver)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        scheduler.advanceTimeBy(0, TimeUnit.MINUTES)

        verifySequence {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES)

        // Compare all items of lists
        assertTrue(viewModel.exchange.value?.rates?.map { rate ->
            rate == list.find { it.currencyCode == rate.currencyCode }
        }?.reduce { acc, b -> acc && b } ?: false)

        confirmVerified(loadingObserver)
    }
}