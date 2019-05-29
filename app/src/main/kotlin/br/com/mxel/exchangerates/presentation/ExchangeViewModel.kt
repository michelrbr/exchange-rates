package br.com.mxel.exchangerates.presentation

import androidx.lifecycle.*
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Rate
import br.com.mxel.exchangerates.domain.usecase.GetExchangeRatesPeriodically
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

    private val _rates = MutableLiveData<List<Rate>?>()
    val rates: LiveData<List<Rate>?>
        get() = _rates

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
            .observeOn(scheduler.mainThread)
            .subscribe { state ->

                if ((state is State.Loading) && EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.increment()
                }

                if (_rates.value == null) {
                    _loading.value = state is State.Loading
                }

                when (state) {
                    is State.Data -> {
                        _rates.value = state.data.rates
                        _error.takeUnless { it.value == null }?.value = null
                        EspressoIdlingResource.decrement()
                    }
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