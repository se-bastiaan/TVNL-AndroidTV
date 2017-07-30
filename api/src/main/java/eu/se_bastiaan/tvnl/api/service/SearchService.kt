package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.search.SearchResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchService {

    @GET("search")
    fun search(@Query("query") query: String, @Query("pageSize") pageSize: Int): Single<SearchResult>

}