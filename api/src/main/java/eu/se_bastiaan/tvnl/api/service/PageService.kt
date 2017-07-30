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

package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.page.Page
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

internal interface PageService {

    @GET
    fun getAbsolutePage(@Url url: String): Single<Page>

    @GET
    fun getAbsolutePage(@Url url: String, @Query("pageSize") pageSize: Int): Single<Page>

    @GET
    fun getAbsoluteUserPage(@Url url: String, @Header("X-Profile-id") profileId: String): Single<Page>

    @GET("/page/profile-home")
    fun getProfileHome(@Header("X-Profile-id") profileId: String, @Query("partyId") partyId: String): Single<Page>

}