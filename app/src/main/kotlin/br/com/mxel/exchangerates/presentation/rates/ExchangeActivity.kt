package br.com.mxel.exchangerates.presentation.rates

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.data.remote.RemoteError
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.presentation.extension.setVisibility
import br.com.mxel.exchangerates.presentation.rates.entity.ExchangeShow
import br.com.mxel.exchangerates.presentation.rates.widget.RateAdapter
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class ExchangeActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    private val viewModel: ExchangeViewModel by currentScope.viewModel(this)

    private val rateAdapter: RateAdapter by lazy { RateAdapter() }

    private val baseCurrencyLabel: AppCompatTextView? by lazy { findViewById<AppCompatTextView>(R.id.base_currency_text_view) }
    private val rateList: RecyclerView? by lazy {
        findViewById<RecyclerView>(R.id.rates_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = rateAdapter
        }
    }
    private val errorLabel: AppCompatTextView? by lazy { findViewById<AppCompatTextView>(R.id.error_text_view) }
    private val loading: ProgressBar? by lazy { findViewById<ProgressBar>(R.id.loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exchange_activity)

        viewModel.exchange.observe(this, Observer { showExchangeRates(it) })
        viewModel.error.observe(this, Observer { showErrorState(it) })
        viewModel.loading.observe(this, Observer { if (it) showLoadingState() })

        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {

        disposable.clear()
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    private fun showErrorState(error: State.Error?) {

        error?.let {

            baseCurrencyLabel?.setVisibility(false)
            rateList?.setVisibility(false)
            loading?.setVisibility(false)
            errorLabel?.setVisibility(true)
            errorLabel?.text = when (it.error) {
                RemoteError.CONNECTION_LOST -> getString(R.string.connection_lost)
                else -> it.cause?.message ?: getString(R.string.generic_error)
            }
        }
    }

    private fun showExchangeRates(exchange: ExchangeShow?) {

        exchange?.let {

            errorLabel?.setVisibility(false)
            baseCurrencyLabel?.setVisibility(true)
            rateList?.setVisibility(true)
            loading?.setVisibility(false)

            baseCurrencyLabel?.text = String.format(
                getString(R.string.currency_label),
                exchange.currencyName,
                exchange.date
            )
            rateAdapter.submitList(it.rates)
        }
    }

    private fun showLoadingState() {

        errorLabel?.setVisibility(false)
        baseCurrencyLabel?.setVisibility(false)
        rateList?.setVisibility(false)
        loading?.setVisibility(true)
    }
}
