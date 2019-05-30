package br.com.mxel.exchangerates.data.remote

import br.com.mxel.exchangerates.data.remote.response.ExchangeApi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val API_BASE_PATH = "https://api.exchangeratesapi.io"

interface ApiClient {

    @GET("latest")
    fun fetchExchangeRates(
        @Query("base") base: String? = "",
        @Query("symbols") symbols: String? = ""
    ): Single<ExchangeApi>
}