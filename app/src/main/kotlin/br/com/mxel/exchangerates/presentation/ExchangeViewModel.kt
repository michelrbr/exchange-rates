package br.com.mxel.exchangerates.presentation

import androidx.lifecycle.*
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.usecase.GetExchangeRatesPeriodically
import br.com.mxel.exchangerates.presentation.entity.ExchangeShow
import br.com.mxel.exchangerates.presentation.util.EspressoIdlingResource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class ExchangeViewModel(
    private val lifecycleOwner: LifecycleOwner,
    private val scheduler: SchedulerProvider,
    private val getCurrencyExchangePeriodically: GetExchangeRatesPeriodically
) : ViewModel(), LifecycleObserver {

    private val disposable = CompositeDisposable()

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
        lifecycleOwner.lifecycle.addObserver(this)
        EspressoIdlingResource.increment()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCurrencyExchange() {
        getCurrencyExchangePeriodically
            .execute(1, TimeUnit.MINUTES, scheduler.backgroundThread)
            .subscribeOn(scheduler.backgroundThread)
            .map {
                // Map to presentation entity
                when (it) {
                    is State.Data -> State.data(ExchangeShow.fromDomain(it.data))
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

    override fun onCleared() {
        lifecycleOwner.lifecycle.removeObserver(this)
        onClearDisposable()
    }
}