package br.com.mxel.exchangerates.presentation.rates.widget

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.presentation.rates.entity.RateShow

class RateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val currencyName by lazy { view.findViewById<AppCompatTextView>(R.id.currency_name) }
    private val currentCurrencyAmount by lazy { view.findViewById<AppCompatTextView>(R.id.current_currency_amount) }
    private val baseCurrencyAmount by lazy { view.findViewById<AppCompatTextView>(R.id.base_currency_amount) }

    fun bindItem(rate: RateShow) {

        currencyName.text = rate.currencyName
        currentCurrencyAmount.text = rate.amountRep
        baseCurrencyAmount.text = rate.amountRep
    }
}
