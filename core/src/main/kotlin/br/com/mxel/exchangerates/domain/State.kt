package br.com.mxel.exchangerates.domain

sealed class State<out T> {

    data class Data<T>(val data: T) : State<T>()

    data class Error(val error: Enum<*>? = null, val cause: Throwable? = null) : State<Nothing>()

    object Loading : State<Nothing>()

    object Idle : State<Nothing>()

    companion object {

        fun <T> data(data: T): State<T> = Data(data)

        fun <T> error(
            error: Enum<*>? = null,
            cause: Throwable? = null
        ): State<T> = Error(error, cause)

        fun <T> loading(): State<T> = Loading

        fun <T> idle(): State<T> = Idle
    }
}