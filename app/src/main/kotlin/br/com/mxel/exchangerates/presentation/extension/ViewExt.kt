package br.com.mxel.exchangerates.presentation.extension

import android.view.View

fun View.setVisibility(show: Boolean) {

    this.visibility = if (show) View.VISIBLE else View.GONE
}