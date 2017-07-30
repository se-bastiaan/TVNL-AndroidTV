package eu.se_bastiaan.tvnl.api.api

import eu.se_bastiaan.tvnl.api.model.search.SearchResult
import eu.se_bastiaan.tvnl.api.service.SearchService
import io.reactivex.Single

class SearchApi internal constructor(private val searchService: SearchService) {

    fun search(query : String, pageSize : Int) : Single<SearchResult> {
        return searchService.search(query, pageSize)
    }

}