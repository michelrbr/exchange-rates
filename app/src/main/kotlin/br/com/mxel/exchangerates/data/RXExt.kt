package br.com.mxel.exchangerates.data

import br.com.mxel.exchangerates.data.rates.remote.response.ErrorApi
import br.com.mxel.exchangerates.data.remote.RemoteError
import br.com.mxel.exchangerates.domain.State
import br.com.mxel.exchangerates.domain.ToDomainMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun <T : ToDomainMapper<R>, R> Single<T>.mapToState(): Single<State<R>> = this
    .map { State.data(it.toDomain()) }
    .onErrorReturn { throwable ->
        when (throwable) {
            is UnknownHostException -> State.error(RemoteError.CONNECTION_LOST)
            is HttpException -> State.error(null, parseError(throwable)?.let { Throwable(it.error) } ?: throwable)
            else -> (State.error(null, throwable))
        }
    }

private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

private fun parseError(error: HttpException): ErrorApi? {

    val json = error.response().errorBody()?.string() ?: ""

    return try {
        moshi.adapter<ErrorApi>(ErrorApi::class.java).fromJson(json)
    } catch (error: IOException) {
        null
    }
}