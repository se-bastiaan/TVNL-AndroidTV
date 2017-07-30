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

package eu.se_bastiaan.tvnl.api.model.entity

import android.support.annotation.StringDef

data class Image(val formats : Map<String, ImageFormat> = HashMap()) {
    fun getImageFormatForDevice(@Format paramString: String): ImageFormat? {
        if (this.formats.containsKey(paramString)) {
            return this.formats[paramString] as ImageFormat
        }

        if (this.formats.containsKey(ORIGINAL)) {
            return this.formats[ORIGINAL] as ImageFormat
        }

        return null
    }

    companion object {
        @StringDef(ORIGINAL, PHONE, TABLET)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Format

        const val ORIGINAL = "original"
        const val PHONE = "phone"
        const val TABLET = "tablet"

        @StringDef(ORIGINAL, CHROMECAST_POST_PLAY, GRID_TILE,
                HEADER, INTERNAL_CHANNEL_lOGO, LANE_TILE, PLAYER_POSTER,
                PLAYER_POST_PLAY, PLAYER_RECOMMENDATION, SEARCH_SUGGESTION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type

        const val CHROMECAST_POST_PLAY = "chromecast.post-play"
        const val GRID_TILE = "grid.tile"
        const val HEADER = "header"
        const val INTERNAL_CHANNEL_lOGO = "channel.logo"
        const val LANE_TILE = "lane.tile"
        const val PLAYER_POSTER = "player.poster"
        const val PLAYER_POST_PLAY = "player.post-play"
        const val PLAYER_RECOMMENDATION = "player.recommendation"
        const val SEARCH_SUGGESTION = "search.suggestion"
    }
}