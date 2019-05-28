package br.com.mxel.exchangerates.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.mxel.exchangerates.BaseTest
import br.com.mxel.exchangerates.domain.entity.Currency
import br.com.mxel.exchangerates.domain.entity.Rates
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
import java.util.*
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

        val usd = Currency("USD", 1.1187, Locale.US.toLanguageTag())
        val pln = Currency("PLN", 4.2974, "pl-PL")

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

        assertTrue((viewModel.rates.value as Rates).usd.toString() == usd.toString())
        assertTrue((viewModel.rates.value as Rates).pln.toString() == pln.toString())

        confirmVerified(loadingObserver)
    }
}