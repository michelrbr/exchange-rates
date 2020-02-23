package br.com.mxel.exchangerates.presentation.rates

import androidx.lifecycle.*
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.rates.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.rates.usecase.GetExchangeRatesPeriodically
import br.com.mxel.exchangerates.presentation.rates.entity.ExchangeShow
import br.com.mxel.exchangerates.presentation.rates.entity.mapper.exchangeToExchangeShow
import br.com.mxel.exchangerates.presentation.util.EspressoIdlingResource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*
import java.util.concurrent.TimeUnit

class ExchangeViewModel(
    private val scheduler: SchedulerProvider,
    private val getCurrencyExchangePeriodically: GetExchangeRatesPeriodically
) : ViewModel(), LifecycleObserver {

    private val disposable = CompositeDisposable()

    private var updateTime: Long = 1L

    private var updateTimeUnit: TimeUnit = TimeUnit.MINUTES

    private var currentCurrency: CurrencyCode? = null

    private var currentCurrencies: List<CurrencyCode>? = null

    private val _exchange = MutableLiveData<ExchangeShow?>()
    val exchange: LiveData<ExchangeShow?>
        get() = _exchange

    private val _loading = MutableLiveData<Boolean>().apply { value = false }
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<State.Error?>().apply { value = null }
    val error: LiveData<State.Error?>
        get() = _error

    init {
        currentCurrency = try {
            CurrencyCode.valueOf(Currency.getInstance(Locale.getDefault()).currencyCode)
        } catch (ie: IllegalArgumentException) {
            CurrencyCode.EUR
        }
        EspressoIdlingResource.increment()
    }

    override fun onCleared() {
        onClearDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCurrencyExchange() {
        getCurrencyExchangePeriodically
            .execute(
                currentCurrency,
                currentCurrencies,
                updateTime,
                updateTimeUnit,
                scheduler.backgroundThread
            )
            .subscribeOn(scheduler.backgroundThread)
            .map {
                // Map to presentation entity
                when (it) {
                    is State.Data -> State.data(exchangeToExchangeShow(it.data))
                    is State.Error -> State.error(it.error, it.cause)
                    is State.Loading -> State.loading()
                    else -> State.idle()
                }
            }
            .observeOn(scheduler.mainThread)
            .subscribe { state ->

                if (_exchange.value == null) {
                    _loading.value = state is State.Loading
                }

                when (state) {
                    is State.Data -> {
                        _exchange.value = state.data
                        _error.takeUnless { it.value == null }?.value = null
                        EspressoIdlingResource.decrement()
                    }
                    is State.Loading -> EspressoIdlingResource.takeIf { it.countingIdlingResource.isIdleNow }?.increment()
                    is State.Error -> _error.value = state
                }
            }
            .addTo(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onClearDisposable() {
        disposable.clear()
    }
}
