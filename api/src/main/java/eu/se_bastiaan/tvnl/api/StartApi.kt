package eu.se_bastiaan.tvnl.api

import eu.se_bastiaan.tvnl.api.manager.PageManager
import eu.se_bastiaan.tvnl.api.service.PageService
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

class StartApi(httpClient: OkHttpClient = OkHttpClient.Builder().build(), baseUrl: HttpUrl) {

    internal val serviceBuilder : ServiceBuilder = ServiceBuilder(httpClient, baseUrl)
    val pageManager : PageManager

    init {
        pageManager = PageManager(serviceBuilder.build(PageService::class.java))
    }

    constructor(httpClient: OkHttpClient) : this(httpClient, HttpUrl.parse("https://start-api.npo.nl/")!!)
    constructor(baseUrl: HttpUrl) : this(OkHttpClient.Builder().build(), baseUrl)
    constructor() : this(HttpUrl.parse("https://start-api.npo.nl/")!!)

}