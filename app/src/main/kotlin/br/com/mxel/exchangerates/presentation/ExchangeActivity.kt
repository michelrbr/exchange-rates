package br.com.mxel.exchangerates.presentation

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.data.remote.RemoteError
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.CurrencyCode
import br.com.mxel.exchangerates.domain.entity.Rate
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ExchangeActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    private val viewModel: ExchangeViewModel by viewModel { parametersOf(this) }

    private val usdLabel: AppCompatTextView? by lazy { findViewById<AppCompatTextView>(R.id.usd_text_view) }
    private val plnLabel: AppCompatTextView? by lazy { findViewById<AppCompatTextView>(R.id.pln_text_view) }
    private val errorLabel: AppCompatTextView? by lazy { findViewById<AppCompatTextView>(R.id.error_text_view) }
    private val loading: ProgressBar? by lazy { findViewById<ProgressBar>(R.id.loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exchange_activity)

        CurrencyCode.values().forEach {
            Timber.d("%s : %s", it.name, Rate(it, 1.34))
        }

        viewModel.rates.observe(this, Observer { showExchangeRates(it) })
        viewModel.error.observe(this, Observer { showErrorState(it) })
        viewModel.loading.observe(this, Observer { if (it) showLoadingState() })
    }

    private fun showErrorState(error: State.Error?) {

        error?.let {

            usdLabel?.setVisibility(false)
            plnLabel?.setVisibility(false)
            loading?.setVisibility(false)
            errorLabel?.setVisibility(true)
            errorLabel?.text = when (it.error) {
                RemoteError.CONNECTION_LOST -> getString(R.string.connection_lost)
                else -> it.cause?.message ?: getString(R.string.generic_error)
            }
        }
    }

    private fun showExchangeRates(rates: List<Rate>?) {

        rates?.let { rate ->

            errorLabel?.setVisibility(false)
            usdLabel?.setVisibility(true)
            plnLabel?.setVisibility(true)
            loading?.setVisibility(false)

            usdLabel?.text =
                String.format(getString(R.string.currency_label), rate.find { it.currencyCode == CurrencyCode.USD })
            plnLabel?.text =
                String.format(getString(R.string.currency_label), rate.find { it.currencyCode == CurrencyCode.PLN })
        }
    }

    private fun showLoadingState() {

        errorLabel?.setVisibility(false)
        usdLabel?.setVisibility(false)
        plnLabel?.setVisibility(false)
        loading?.setVisibility(true)
    }

    override fun onDestroy() {

        disposable.clear()
        super.onDestroy()
    }
}