package br.com.mxel.exchangerates.presentation.widget

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.presentation.entity.RateShow

class RateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val currencyName by lazy { view.findViewById<AppCompatTextView>(R.id.currency_name_text_view) }
    private val currencyAmount by lazy { view.findViewById<AppCompatTextView>(R.id.currency_amount_text_view) }

    fun bindItem(rate: RateShow) {

        currencyAmount.text = rate.amountRep
        currencyName.text = rate.currencyName
    }
}