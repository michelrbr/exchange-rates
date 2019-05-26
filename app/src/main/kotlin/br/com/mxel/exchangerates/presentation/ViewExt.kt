package br.com.mxel.exchangerates.presentation

import android.view.View

fun View.setVisibility(show: Boolean) {

    this.visibility = if (show) View.VISIBLE else View.GONE
}