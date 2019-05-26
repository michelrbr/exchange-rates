package br.com.mxel.exchangerates.data.remote

import br.com.mxel.exchangerates.data.remote.response.ExchangeApi
import io.reactivex.Single
import retrofit2.http.GET

const val API_BASE_PATH = "https://api.exchangeratesapi.io"

interface ApiClient {

    @GET("latest?symbols=USD,PLN")
    fun getMoviesOrderedBy(): Single<ExchangeApi>
}
