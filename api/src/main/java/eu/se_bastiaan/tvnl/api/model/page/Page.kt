package eu.se_bastiaan.tvnl.api.model.page

import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.AbstractComponent


data class Page(val components: List<AbstractComponent> = ArrayList(),
                val isOnlyNpoPlus: Boolean = false, val hasPlaceHoldersReplaced : Boolean = false,
                @SerializedName("_links") val links: Map<String, Link> = HashMap(),
                val pageType : Int?, val pageUrl : String = "",
                val requiresAuthentication : Boolean = false, val subtitle : String?,
                val title : String?)