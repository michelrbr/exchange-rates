package br.com.mxel.exchangerates.presentation

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.data.remote.RemoteError
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.entity.Rates
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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

    private fun showExchangeRates(rates: Rates?) {

        rates?.let {

            errorLabel?.setVisibility(false)
            usdLabel?.setVisibility(true)
            plnLabel?.setVisibility(true)
            loading?.setVisibility(false)

            usdLabel?.text = String.format(getString(R.string.currency_label), it.usd)
            plnLabel?.text = String.format(getString(R.string.currency_label), it.pln)
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