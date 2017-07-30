package eu.se_bastiaan.tvnl.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyHeaderInterceptor : Interceptor {

    internal val headerKey : String = "ApiKey"
    internal val headerVal : String = "07896f1ee72645f68bc75581d7f00d54"

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val builder = request.newBuilder()

        builder.addHeader(headerKey, headerVal)

        return chain.proceed(builder.build())
    }

}