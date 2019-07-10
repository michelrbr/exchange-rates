package br.com.mxel.exchangerates.presentation.rates.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.presentation.rates.entity.RateShow

class RateAdapter : RecyclerView.Adapter<RateViewHolder>() {

    private var rates: List<RateShow>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {

        return RateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rate_view_holder, parent, false))
    }

    override fun getItemCount(): Int = rates?.size ?: 0

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {

        rates?.run {
            holder.bindItem(get(position))
        }
    }

    fun submitList(rateList: List<RateShow>) {
        rates = rateList
        notifyDataSetChanged()
    }
}