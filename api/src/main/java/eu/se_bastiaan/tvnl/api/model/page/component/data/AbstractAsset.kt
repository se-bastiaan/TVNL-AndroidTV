package eu.se_bastiaan.tvnl.api.model.page.component.data

import android.support.annotation.StringDef
import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link

abstract class AbstractAsset(
        val broadcasters: List<String> = ArrayList(), val description: String = "",
        val id: String, val images: Map<String, Image> = HashMap(),
        val isOnlyOnNpoPlus: Boolean = false, val links: Map<String, Link> = HashMap(),
        val onDemand: Boolean = false, val title: String = "",
        @ItemType val type: String = "") {
    companion object {
        @StringDef(ARCHIVE, BROADCAST, CLIP, COLLECTION, FRAGMENT,
                LIVE_RADIO, LIVE_TV, PLAYLIST, PROMO, RADIO_CHANNEL,
                SEASON, SERIES, STRAND)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ItemType

        const val ARCHIVE = "archive"
        const val BROADCAST = "broadcast"
        const val CLIP = "clip"
        const val COLLECTION = "collection"
        const val FRAGMENT = "fragment"
        const val LIVE_RADIO = "liveradio"
        const val LIVE_TV = "livetv"
        const val PLAYLIST = "playlist"
        const val PROMO = "promo"
        const val RADIO_CHANNEL = "RadioChannel"
        const val SEASON = "season"
        const val SERIES = "series"
        const val STRAND = "strand"
    }
}