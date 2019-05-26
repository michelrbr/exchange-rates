package br.com.mxel.exchangerates.data.remote

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ExchangeTestInterceptor : BaseTestInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {

        val (code, response) = getResponse(buildJsonPath(chain.request().url()))

        return buildResponse(response, chain.request(), code)
    }

    override fun buildJsonPath(url: HttpUrl): String {

        val fileName = url.pathSegments()[0]

        return getFilePath("$fileName.json")
    }
}