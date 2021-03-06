package br.com.mxel.exchangerates.data.remote

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.File

abstract class BaseTestInterceptor : Interceptor {

    protected fun getResponse(path: String): Pair<Int, String> {
        return File(path)
            .takeIf { it.exists() }
            ?.let { Pair(200, String(it.readBytes())) }
            ?: Pair(
                404,
                String(File(getFilePath("error_resource_not_found.json")).readBytes())
            )
    }

    protected fun getFilePath(path: String): String {
        return this.javaClass.classLoader?.getResource(path)?.path ?: ""
    }

    protected fun buildResponse(body: String, request: Request, code: Int = 200): Response {
        return Response.Builder()
            .code(code)
            .message(code.takeIf { it == 200 }?.let { "OK" } ?: "Error")
            .request(request)
            .protocol(Protocol.HTTP_1_0)
            .body(body.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }

    protected abstract fun buildJsonPath(url: HttpUrl): String
}
