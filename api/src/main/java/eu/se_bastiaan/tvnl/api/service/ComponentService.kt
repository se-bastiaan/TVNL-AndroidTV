package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.page.component.Data
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ComponentService {
    @GET
    fun getComponentData(@Url url: String): Single<Data>
}