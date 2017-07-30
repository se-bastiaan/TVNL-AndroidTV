package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.page.Page
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface PageService {

    @GET
    fun getAbsolutePage(@Url url: String): Single<Page>

    @GET
    fun getAbsolutePage(@Url url: String, @Query("pageSize") pageSize: Int): Single<Page>

    @GET
    fun getAbsoluteUserPage(@Url url: String, @Header("X-Profile-id") profileId: String): Single<Page>

    @GET("/page/profile-home")
    fun getProfileHome(@Header("X-Profile-id") profileId: String, @Query("partyId") partyId: String): Single<Page>

}