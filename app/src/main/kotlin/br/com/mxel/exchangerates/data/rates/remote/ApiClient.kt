package br.com.mxel.exchangerates.data.rates.remote

import br.com.mxel.exchangerates.data.rates.remote.response.ExchangeApi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val API_BASE_PATH = "https://api.exchangeratesapi.io"

interface ApiClient {

    @GET("latest")
    fun fetchExchangeRates(
        @Query("base") baseCurrency: String? = "",
        @Query("symbols") currencies: String? = ""
    ): Single<ExchangeApi>
}