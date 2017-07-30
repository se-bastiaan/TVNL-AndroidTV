/*
 * Copyright (C) 2017 Sébastiaan (github.com/se-bastiaan)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see . Also add information on how to contact you by electronic and paper mail.
 */

package eu.se_bastiaan.tvnl.api

import com.google.gson.GsonBuilder
import eu.se_bastiaan.tvnl.api.api.*
import eu.se_bastiaan.tvnl.api.deserializer.AbstractAssetDeserializer
import eu.se_bastiaan.tvnl.api.interceptor.ApiKeyHeaderInterceptor
import eu.se_bastiaan.tvnl.api.model.page.component.AbstractComponent
import eu.se_bastiaan.tvnl.api.deserializer.AbstractComponentDeserializer
import eu.se_bastiaan.tvnl.api.deserializer.ZonedDateTimeDeserializer
import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset
import eu.se_bastiaan.tvnl.api.service.*
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

    val page: PageApi
    val menu: MenuApi
    val epg: EpgApi
    val component: ComponentApi
    val search: SearchApi

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

        page = PageApi(retrofit.create(PageService::class.java))
        menu = MenuApi(retrofit.create(MenuService::class.java))
        epg = EpgApi(retrofit.create(EpgService::class.java))
        component = ComponentApi(retrofit.create(ComponentService::class.java))
        search = SearchApi(retrofit.create(SearchService::class.java))
    }

    constructor(httpClient: OkHttpClient) : this(httpClient, HttpUrl.parse("https://start-api.npo.nl/")!!)
    constructor(baseUrl: HttpUrl) : this(OkHttpClient.Builder().build(), baseUrl)
    constructor() : this(HttpUrl.parse("https://start-api.npo.nl/")!!)

}