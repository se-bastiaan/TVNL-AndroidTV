package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.epg.EpgModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


internal interface EpgService {

    @GET("epg/{date}")
    fun getEpg(@Path("date") date: String): Single<EpgModel>

    @GET("epg/{date}")
    fun getEpg(@Path("date") date: String, @Query("type") type: String): Single<EpgModel>

}