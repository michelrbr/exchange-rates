package br.com.mxel.exchangerates.presentation

import androidx.lifecycle.*
import br.com.mxel.exchangerates.domain.SchedulerProvider
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Rates
import br.com.mxel.exchangerates.domain.usecase.GetExchangeRatesPeriodically
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class ExchangeViewModel(
    private val lifecycleOwner: LifecycleOwner,
    private val scheduler: SchedulerProvider,
    private val getCurrencyExchangePeriodically: GetExchangeRatesPeriodically
) : ViewModel(), LifecycleObserver {

    private val disposable = CompositeDisposable()

    private val _rates = MutableLiveData<Rates?>()
    val rates: LiveData<Rates?>
        get() = _rates

    private val _loading = MutableLiveData<Boolean>().apply { value = false }
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<State.Error?>().apply { value = null }
    val error: LiveData<State.Error?>
        get() = _error

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCurrencyExchange() {
        getCurrencyExchangePeriodically
            .execute(1, TimeUnit.MINUTES, scheduler.backgroundThread)
            .subscribeOn(scheduler.backgroundThread)
            .observeOn(scheduler.mainThread)
            .subscribe { state ->

                if (_rates.value == null) {
                    _loading.value = state is State.Loading
                }

                when (state) {
                    is State.Data -> {
                        _rates.value = state.data.rates
                        _error.takeUnless { it.value == null }?.value = null
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