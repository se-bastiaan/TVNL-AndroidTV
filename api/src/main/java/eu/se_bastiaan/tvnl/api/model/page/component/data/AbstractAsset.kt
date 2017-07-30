/*
 * Copyright (C) 2017 Sébastiaan (github.com/se-bastiaan)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see . Also add information on how to contact you by electronic and paper mail.
 */

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