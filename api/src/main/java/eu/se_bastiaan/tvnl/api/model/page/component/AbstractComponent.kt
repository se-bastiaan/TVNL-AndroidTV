package eu.se_bastiaan.tvnl.api.model.page.component

import android.support.annotation.StringDef
import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter

abstract class AbstractComponent(val data : Data = Data(), val filter : Filter = Filter(),
                                 val isPlaceHolder : Boolean = false,
                                 @SerializedName("_links") val links: Map<String, Link> = HashMap(),
                                 val mediaTarget: String, val target: String,
                                 val targetedBy: List<String>,
                                 val targets : List<String> = ArrayList(), val title : String?,
                                 @ComponentType val type : String, val id : String
                        ) {
    var isReplacingPlaceHolder : Boolean = false

    fun getLink(key: String): Link? {
        if (links.containsKey(key)) {
            return links[key] as Link
        }
        return null
    }

    companion object {
        @StringDef(BANNER, COLLECTION_HEADER, CONTINUE_WATCHING, EPISODE_HEADER,
                ERROR, FILTER, FIVE_GRID, FRAGMENT, FRANCHISE_HEADER, GRID, LANE,
                LIVE_HEADER, LOAD_MORE, NOW_PLAYING, PICKER, RECOMMENDATIONS,
                SERIES, SPOTLIGHT_HEADER, SUBSCRIPTION, TABS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ComponentType

        const val BANNER = "banner"
        const val COLLECTION_HEADER = "collectionheader"
        const val CONTINUE_WATCHING = "continuewatching"
        const val EPISODE_HEADER = "episodeheader"
        const val ERROR = "error"
        const val FILTER = "filter"
        const val FIVE_GRID = "fivegrid"
        const val FRAGMENT = "fragment"
        const val FRANCHISE_HEADER = "franchiseheader"
        const val GRID = "grid"
        const val LANE = "lane"
        const val LIVE_HEADER = "liveheader"
        const val LOAD_MORE = "loadmore"
        const val NOW_PLAYING = "nowplaying"
        const val PICKER = "picker"
        const val RECOMMENDATIONS = "recommendations"
        const val SERIES = "series"
        const val SPOTLIGHT_HEADER = "spotlightheader"
        const val SUBSCRIPTION = "subscription"
        const val TABS = "tabs"
    }
}