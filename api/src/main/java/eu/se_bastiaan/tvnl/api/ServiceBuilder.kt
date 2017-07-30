package eu.se_bastiaan.tvnl.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder(httpClient: OkHttpClient, baseUrl: HttpUrl) {

    val retrofit : Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()

    fun <T> build(service : Class<T>) : T {
        return retrofit.create(service)
    }

}