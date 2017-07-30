package eu.se_bastiaan.tvnl.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class DeviceInfoHeaderInterceptor(val platform : String, val deviceInfo : String) : Interceptor {

    internal val platformHeaderKey = "X-Npo-Platform"
    internal val deviceHeaderKey = "X-Npo-Device"

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val builder = request.newBuilder()

        builder.addHeader(platformHeaderKey, platform)
        builder.addHeader(deviceHeaderKey, deviceInfo)

        return chain.proceed(builder.build())
    }

}