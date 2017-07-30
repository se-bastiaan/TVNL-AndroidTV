package eu.se_bastiaan.tvnl.api

import com.google.gson.GsonBuilder
import eu.se_bastiaan.tvnl.api.deserializer.AbstractAssetDeserializer
import eu.se_bastiaan.tvnl.api.interceptor.ApiKeyHeaderInterceptor
import eu.se_bastiaan.tvnl.api.manager.PageManager
import eu.se_bastiaan.tvnl.api.model.page.component.AbstractComponent
import eu.se_bastiaan.tvnl.api.deserializer.AbstractComponentDeserializer
import eu.se_bastiaan.tvnl.api.deserializer.ZonedDateTimeDeserializer
import eu.se_bastiaan.tvnl.api.manager.MenuManager
import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset
import eu.se_bastiaan.tvnl.api.service.MenuService
import eu.se_bastiaan.tvnl.api.service.PageService
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class StartApi(httpClient: OkHttpClient = OkHttpClient.Builder().build(),
               baseUrl: HttpUrl) {

    internal val retrofit : Retrofit

    val pageManager : PageManager
    val menuManager : MenuManager

    init {
        val gson = GsonBuilder()
                .registerTypeAdapter(AbstractAsset::class.java, AbstractAssetDeserializer())
                .registerTypeAdapter(AbstractComponent::class.java, AbstractComponentDeserializer())
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
                .create()

        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(httpClient.newBuilder().addInterceptor(ApiKeyHeaderInterceptor()).build())
                .build()

        pageManager = PageManager(retrofit.create(PageService::class.java))
        menuManager = MenuManager(retrofit.create(MenuService::class.java))
    }

    constructor(httpClient: OkHttpClient) : this(httpClient, HttpUrl.parse("https://start-api.npo.nl/")!!)
    constructor(baseUrl: HttpUrl) : this(OkHttpClient.Builder().build(), baseUrl)
    constructor() : this(HttpUrl.parse("https://start-api.npo.nl/")!!)

}