package eu.se_bastiaan.tvnl.api.model.search

import eu.se_bastiaan.tvnl.api.model.page.component.data.Asset

data class SearchResult (val count : Int = 0, val items : List<Asset> = ArrayList(),
                         val total : Int = 0)