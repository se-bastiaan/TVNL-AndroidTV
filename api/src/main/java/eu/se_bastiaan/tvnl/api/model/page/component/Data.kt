package eu.se_bastiaan.tvnl.api.model.page.component

import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset

data class Data(val count : Int = 0, val items : List<AbstractAsset> = ArrayList(),
                @SerializedName("_links") val links : Map<String, Link> = HashMap(),
                val total : Int = 0) {
    fun getLink(key: String): Link? {
        if (this.links.containsKey(key)) {
            return this.links[key]
        }
        return null
    }
}